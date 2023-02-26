package com.araujo.cashbackservice.config;

import java.util.HashMap;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.HeadersExchange;
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

    public static final String ORDER_EVENT_HEADER_NAME = "order-event-type";
    public static final String CUSTUMER_TYPE_HEADER_NAME = "custumer-type";

    public static final String ORDER_PAID_EVENT_HEADER_VALUE = "order.paid";
    public static final String ORDER_CANCEL_EVENT_HEADER_VALUE = "order.cancel";

    public static final String CUSTUMER_BASIC_HEADER_VALUE = "basic";
    public static final String CUSTUMER_PREMIUM_HEADER_VALUE = "premium";

    public static final String CASHBACK_BASIC_QUEUE_NAME = "cashback.v1.on-paid-basic.generate-cashback";
    public static final String CASHBACK_VIP_QUEUE_NAME = "cashback.v1.on-paid-vip.generate-cashback";

    public static final String CASHBACK_CANCEL_QUEUE_NAME = "cashback.v1.on-paid-cancel-basic.cancel-cashback";

    @Bean
    public Queue queueGenerateCashbackBasic() {
        return new Queue(CASHBACK_BASIC_QUEUE_NAME);
    }

    @Bean
    public Queue queueGenerateCashbackVip() {
        return new Queue(CASHBACK_VIP_QUEUE_NAME);
    }

    @Bean
    public Queue queueGenerateCancelCashback() {
        return new Queue(CASHBACK_CANCEL_QUEUE_NAME);
    }

    @Bean
    public Binding bindingGenerateCashbackBasic() {
        var queue = queueGenerateCashbackBasic();
        var exchange = new HeadersExchange(ORDER_EXCHANGE_NAME);

        HashMap<String, Object> headers = new HashMap<>();
        headers.put(ORDER_EVENT_HEADER_NAME, ORDER_PAID_EVENT_HEADER_VALUE);
        headers.put(CUSTUMER_TYPE_HEADER_NAME, CUSTUMER_BASIC_HEADER_VALUE);

        return BindingBuilder.bind(queue).to(exchange).whereAll(headers).match();
    }

    @Bean
    public Binding bindingGenerateCashbackVip() {
        var queue = queueGenerateCashbackVip();
        var exchange = new HeadersExchange(ORDER_EXCHANGE_NAME);

        HashMap<String, Object> headers = new HashMap<>();
        headers.put(ORDER_EVENT_HEADER_NAME, ORDER_PAID_EVENT_HEADER_VALUE);
        headers.put(CUSTUMER_TYPE_HEADER_NAME, CUSTUMER_PREMIUM_HEADER_VALUE);

        return BindingBuilder.bind(queue).to(exchange).whereAll(headers).match();
    }

    @Bean
    public Binding bindingCancelCashback() {
        var queue = queueGenerateCancelCashback();
        var exchange = new HeadersExchange(ORDER_EXCHANGE_NAME);
        return BindingBuilder.bind(queue).to(exchange).where(ORDER_EVENT_HEADER_NAME)
                .matches(ORDER_CANCEL_EVENT_HEADER_VALUE);
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
