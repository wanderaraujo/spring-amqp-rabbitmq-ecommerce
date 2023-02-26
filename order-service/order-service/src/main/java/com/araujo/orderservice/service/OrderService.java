package com.araujo.orderservice.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.araujo.orderservice.config.RabbitMQConfig;
import com.araujo.orderservice.model.OrderModel;

@Service
public class OrderService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void payOrder(OrderModel order) {
        this.rabbitTemplate.convertAndSend(RabbitMQConfig.ORDER_EXCHANGE_NAME, RabbitMQConfig.ORDER_PAID_ROUTING_KEY,
                order);
    }

    public void cancelOrder(OrderModel order) {
        this.rabbitTemplate.convertAndSend(RabbitMQConfig.ORDER_EXCHANGE_NAME, RabbitMQConfig.ORDER_CANCEL_ROUTING_KEY,
                order);
    }

}
