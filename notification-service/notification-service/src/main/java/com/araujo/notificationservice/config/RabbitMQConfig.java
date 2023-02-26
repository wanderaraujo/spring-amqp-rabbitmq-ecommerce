package com.araujo.notificationservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
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

    public static final String ORDER_EXCHANGE_NAME = "orders.v1.events";

    public static final String ORDER_PAID_ROUTING_KEY = "order.paid";
    public static final String ORDER_CANCEL_ROUTING_KEY = "order.cancel";

    public static final String NOTIFICATION_QUEUE_SUCESS_PAYMENT = "notifications.v1.order-paid.sucess-payment";
    public static final String NOTIFICATION_QUEUE_CANCEL_PAYMENT = "notifications.v1.order-paid.cancel-payment";

    @Bean
    public Queue queueSucessPayment() {
        return new Queue(NOTIFICATION_QUEUE_SUCESS_PAYMENT);
    }

    @Bean
    public Queue queueCancelPayment() {
        return new Queue(NOTIFICATION_QUEUE_CANCEL_PAYMENT);
    }

    @Bean
    public Binding bindingCancelPaymentNotification() {
        var queue = queueCancelPayment();
        var exchange = new DirectExchange(ORDER_EXCHANGE_NAME);
        return BindingBuilder.bind(queue).to(exchange).with(ORDER_CANCEL_ROUTING_KEY);
    }

    @Bean
    public Binding bindingSucessPaymentNotification() {
        var queue = queueSucessPayment();
        var exchange = new DirectExchange(ORDER_EXCHANGE_NAME);
        return BindingBuilder.bind(queue).to(exchange).with(ORDER_PAID_ROUTING_KEY);
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
