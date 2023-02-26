package com.araujo.orderservice.service;

import java.math.BigDecimal;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.araujo.orderservice.config.RabbitMQConfig;
import com.araujo.orderservice.model.OrderModel;

@Service
public class OrderService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void createOder(OrderModel order) {
        this.rabbitTemplate.convertAndSend(RabbitMQConfig.ORDER_EXCHANGE_NAME, RabbitMQConfig.ORDER_CREATED,
                order);
    }

    public void payOrder(OrderModel order) {

        String rountingKey;
        if (order.getValue().compareTo(new BigDecimal("100")) > 0) {
            rountingKey = RabbitMQConfig.ORDER_PAID_VIP_ROUTING_KEY;
        } else {
            rountingKey = RabbitMQConfig.ORDER_PAID_BASIC_ROUTING_KEY;
        }

        this.rabbitTemplate.convertAndSend(RabbitMQConfig.ORDER_EXCHANGE_NAME, rountingKey,
                order);
    }

    public void cancelOrder(OrderModel order) {

        String rountingKey;
        if (order.getValue().compareTo(new BigDecimal("100")) > 0) {
            rountingKey = RabbitMQConfig.ORDER_CANCEL_VIP_ROUTING_KEY;
        } else {
            rountingKey = RabbitMQConfig.ORDER_CANCEL_BASIC_ROUTING_KEY;
        }

        this.rabbitTemplate.convertAndSend(RabbitMQConfig.ORDER_EXCHANGE_NAME, rountingKey,
                order);
    }

}
