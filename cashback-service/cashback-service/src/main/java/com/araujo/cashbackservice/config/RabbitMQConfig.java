package com.araujo.cashbackservice.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String ORDER_EXCHANGE_NAME = "orders.v1.order-created";
    public static final String ORDER_EXCHANGE_NAME_DLX = "orders.v1.order-created.dlx";
    public static final String ORDER_QUEUE_NAME_CASHBACK = "orders.v1.order-created.generate-cashback";
    public static final String ORDER_QUEUE_NAME_CASHBACK_DLQ = "orders.v1.order-created.dlx.generate-cashback.dlq";
    public static final String ORDER_QUEUE_NAME_CASHBACK_DLQ_LOT = "orders.v1.order-created.dlx.generate-cashback.dlq.parking-lot";

    @Bean
    public Queue queueCashBack() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", ORDER_EXCHANGE_NAME_DLX);
        // args.put("x-dead-letter-routing-key", ORDER_QUEUE_NAME_CASHBACK_DLQ); // send
        // direct to queue
        return new Queue(ORDER_QUEUE_NAME_CASHBACK, true, false, false, args);
    }

    @Bean
    public Queue queueCashBackDLQ() {
        return new Queue(ORDER_QUEUE_NAME_CASHBACK_DLQ);
    }

    @Bean
    public Queue queueCashBackParkinLot() {
        return new Queue(ORDER_QUEUE_NAME_CASHBACK_DLQ_LOT);
    }

    @Bean
    public Binding binding() {
        var queue = queueCashBack();
        var exchange = new FanoutExchange(ORDER_EXCHANGE_NAME);
        return BindingBuilder.bind(queue).to(exchange);
    }

    @Bean
    public Binding bindingDLQ() {
        var queue = queueCashBackDLQ();
        var exchange = new FanoutExchange(ORDER_EXCHANGE_NAME_DLX);
        return BindingBuilder.bind(queue).to(exchange);
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
            Jackson2JsonMessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }

    @Bean
    public ApplicationListener<ApplicationReadyEvent> applicationReadyEventApplicationListener(
            RabbitAdmin rabbitAdmin) {
        return event -> rabbitAdmin.initialize();
    }

}
