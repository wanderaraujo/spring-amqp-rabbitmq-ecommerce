package com.araujo.notificationservice.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.araujo.notificationservice.model.OrderModel;

import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class OrderCreatedListener {

    public static final String ORDER_QUEUE_NAME_NOTIFICATION = "orders.v1.order-created.send-notification";

    @RabbitListener(queues = ORDER_QUEUE_NAME_NOTIFICATION)
    public void onOrderCreated(OrderModel message) {

        log.info("message received {}", message.toString());

    }

}
