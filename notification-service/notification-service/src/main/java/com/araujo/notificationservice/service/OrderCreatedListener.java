package com.araujo.notificationservice.service;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.araujo.notificationservice.config.RabbitMQConfig;
import com.araujo.notificationservice.model.OrderModel;

import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class OrderCreatedListener {

    @RabbitListener(queues = RabbitMQConfig.NOTIFICATIONS_ORDER_QUEUE)
    public void onNotification(OrderModel message, Message me) {
        log.info("Notification {} detail {}", message, me.getMessageProperties().getReceivedRoutingKey());
    }

}
