package com.araujo.orderservice.config;

import org.springframework.amqp.core.HeadersExchange;
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

    public static final String ORDER_EVENT_HEADER_NAME = "order-event-type";
    public static final String CUSTUMER_TYPE_HEADER_NAME = "custumer-type";

    public static final String ORDER_PAID_EVENT_HEADER_VALUE = "order.paid";
    public static final String ORDER_CANCEL_EVENT_HEADER_VALUE = "order.cancel";

    public static final String CUSTUMER_BASIC_HEADER_VALUE = "basic";
    public static final String CUSTUMER_PREMIUM_HEADER_VALUE = "premium";

    @Bean
    public HeadersExchange exchange() {
        return new HeadersExchange(ORDER_EXCHANGE_NAME);
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public ApplicationListener<ApplicationReadyEvent> applicationReadyEventApplicationListener(
            RabbitAdmin rabbitAdmin) {
        return event -> rabbitAdmin.initialize();
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

}
