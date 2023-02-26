package com.araujo.orderservice.service;

import java.math.BigDecimal;

import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.araujo.orderservice.config.RabbitMQConfig;
import com.araujo.orderservice.model.OrderModel;

@Service
public class OrderService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void createOrder(OrderModel order) {

        final int priority;

        // simulate business logic to set priority message level
        if (order.getValue().compareTo(new BigDecimal("2000")) > 0) {
            priority = 5;
        } else {
            priority = 1;
        }

        final MessagePostProcessor processor = message -> {
            var propertiesMessage = message.getMessageProperties();
            propertiesMessage.setPriority(priority);
            return message;
        };

        this.rabbitTemplate.convertAndSend(RabbitMQConfig.ORDER_EXCHANGE_NAME, "", order, processor);
    }

}
