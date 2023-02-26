package com.araujo.notificationservice.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.araujo.notificationservice.config.RabbitMQConfig;
import com.araujo.notificationservice.model.OrderModel;

import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class OrderCreatedListener {

    @RabbitListener(queues = RabbitMQConfig.NOTIFICATION_QUEUE_SUCESS_PAYMENT)
    public void notificationSucess(OrderModel message) {
        log.info("Greate news your payment {} has been approved ;)", message.getValue());
    }

    @RabbitListener(queues = RabbitMQConfig.NOTIFICATION_QUEUE_CANCEL_PAYMENT)
    public void notificationCancel(OrderModel message) {
        log.info("Sorry your limit is not enough to pay {} change your payment method", message.getValue());
    }

}
