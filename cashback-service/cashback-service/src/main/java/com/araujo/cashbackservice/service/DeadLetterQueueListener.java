package com.araujo.cashbackservice.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;

import com.araujo.cashbackservice.config.RabbitMQConfig;
import com.araujo.cashbackservice.model.OrderModel;

import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class DeadLetterQueueListener {

    private static final String X_RETRY_HEADER = "x-dlq-retry";

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = RabbitMQConfig.ORDER_QUEUE_NAME_CASHBACK_DLQ)
    public void process(OrderModel message, @Headers Map<String, Object> headers) {
        Integer retryHeader = (Integer) headers.get(X_RETRY_HEADER);

        if (retryHeader == null) {
            retryHeader = 0;
        }

        log.info("Retry process sale {}", message);

        if (retryHeader < 3) {
            Map<String, Object> updateHeaders = new HashMap<>(headers);
            int tryCount = retryHeader + 1;
            updateHeaders.put(X_RETRY_HEADER, tryCount);

            // Reprocess

            final MessagePostProcessor messagePostProcessor = m -> {
                var messageProperties = m.getMessageProperties();
                updateHeaders.forEach(messageProperties::setHeader);
                return m;
            };

            log.info("Resending process sale {} to DLQ", message);

            this.rabbitTemplate.convertAndSend(RabbitMQConfig.ORDER_QUEUE_NAME_CASHBACK_DLQ, message,
                    messagePostProcessor);

        } else {
            log.info("reprocess fail with max attemps moving to parkin-lot queue");
            this.rabbitTemplate.convertAndSend(RabbitMQConfig.ORDER_QUEUE_NAME_CASHBACK_DLQ_LOT, message);
        }
    }

}
