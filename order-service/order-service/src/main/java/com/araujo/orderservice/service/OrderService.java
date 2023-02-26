package com.araujo.orderservice.service;

import java.math.BigDecimal;

import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.araujo.orderservice.config.RabbitMQConfig;
import com.araujo.orderservice.model.OrderModel;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class OrderService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void createOder(OrderModel order) {
        log.info("Create order {}", order);
    }

    public void payOrder(OrderModel order) {

        String rountingKey;
        if (order.getValue().compareTo(new BigDecimal("100")) > 0) {
            rountingKey = RabbitMQConfig.CUSTUMER_PREMIUM_HEADER_VALUE;
        } else {
            rountingKey = RabbitMQConfig.CUSTUMER_BASIC_HEADER_VALUE;
        }

        MessagePostProcessor messagePostProcessor = message -> {
            MessageProperties messageProperties = message.getMessageProperties();
            messageProperties.setHeader(RabbitMQConfig.ORDER_EVENT_HEADER_NAME,
                    RabbitMQConfig.ORDER_PAID_EVENT_HEADER_VALUE);
            messageProperties.setHeader(RabbitMQConfig.CUSTUMER_TYPE_HEADER_NAME, rountingKey);
            return message;
        };

        this.rabbitTemplate.convertAndSend(RabbitMQConfig.ORDER_EXCHANGE_NAME, "",
                order, messagePostProcessor);
    }

    public void cancelOrder(OrderModel order) {

        String rountingKey;
        if (order.getValue().compareTo(new BigDecimal("100")) > 0) {
            rountingKey = RabbitMQConfig.CUSTUMER_PREMIUM_HEADER_VALUE;
        } else {
            rountingKey = RabbitMQConfig.CUSTUMER_BASIC_HEADER_VALUE;
        }

        MessagePostProcessor messagePostProcessor = message -> {
            MessageProperties messageProperties = message.getMessageProperties();
            messageProperties.setHeader(RabbitMQConfig.ORDER_EVENT_HEADER_NAME,
                    RabbitMQConfig.ORDER_CANCEL_EVENT_HEADER_VALUE);
            messageProperties.setHeader(RabbitMQConfig.CUSTUMER_TYPE_HEADER_NAME, rountingKey);
            return message;
        };

        this.rabbitTemplate.convertAndSend(RabbitMQConfig.ORDER_EXCHANGE_NAME, "",
                order, messagePostProcessor);
    }

}
