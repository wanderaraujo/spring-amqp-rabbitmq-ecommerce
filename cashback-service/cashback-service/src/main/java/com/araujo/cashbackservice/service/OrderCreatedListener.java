package com.araujo.cashbackservice.service;

import java.math.BigDecimal;

import javax.management.RuntimeErrorException;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.araujo.cashbackservice.model.OrderModel;

import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class OrderCreatedListener {

    public static final String ORDER_QUEUE_NAME_CASHBACK = "orders.v1.order-created.generate-cashback";

    @RabbitListener(queues = ORDER_QUEUE_NAME_CASHBACK)
    public void onOrderCreated(OrderModel message) {

        if (message.getValue().compareTo(new BigDecimal("10000")) > 0) {
            throw new RuntimeException("Falha no processamento na venda de id " + message.getProductName());
        }

        log.info("message received {}", message.toString());

    }

}
