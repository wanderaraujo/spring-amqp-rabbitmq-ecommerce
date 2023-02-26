package com.araujo.cashbackservice.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.araujo.cashbackservice.config.RabbitMQConfig;
import com.araujo.cashbackservice.model.OrderModel;

import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class OrderCreatedListener {

    @RabbitListener(queues = RabbitMQConfig.CASHBACK_BASIC_QUEUE_NAME)
    public void onOrderPaidBasic(OrderModel message) {
        log.info("Cashback Cliente Basic generate sucessful {}", message);
    }

    @RabbitListener(queues = RabbitMQConfig.CASHBACK_VIP_QUEUE_NAME)
    public void onOrderPaidVip(OrderModel message) {
        log.info("Cashback Cliente Vip generate sucessful {}", message);
    }

    @RabbitListener(queues = RabbitMQConfig.CASHBACK_CANCEL_QUEUE_NAME)
    public void onOrderCancel(OrderModel message) {
        log.info("Cashback canceled {}", message);
    }

}
