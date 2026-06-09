package com.studyflow.reservation.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMqConfig {
    // 交换机名称
    public static final String EXCHANGE_RESERVE = "reserve.direct";
    public static final String EXCHANGE_CHECKIN = "checkin.direct";

    // 队列名称
    public static final String QUEUE_TIMEOUT = "reserve.timeout.queue";
    public static final String QUEUE_CANCEL = "reserve.cancel.queue";
    public static final String QUEUE_CHECKIN = "checkin.done.queue";

    // 死信队列名称
    public static final String QUEUE_DLX = "reserve.dlx.queue";

    // 路由键
    public static final String RK_TIMEOUT = "reserve.timeout";
    public static final String RK_CANCEL = "reserve.cancel";
    public static final String RK_CHECKIN = "checkin.done";
    public static final String RK_DLX = "reserve.dlx";

    @Bean
    public DirectExchange reserveExchange() {
        return new DirectExchange(EXCHANGE_RESERVE);
    }

    @Bean
    public DirectExchange checkinExchange() {
        return new DirectExchange(EXCHANGE_CHECKIN);
    }

    @Bean
    public Queue timeoutQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-message-ttl", 15 * 60 * 1000);
        args.put("x-dead-letter-exchange", EXCHANGE_RESERVE);
        args.put("x-dead-letter-routing-key", "reserve.dlx");
        return new Queue(QUEUE_TIMEOUT, true, false, false, args);
    }

    @Bean
    public Queue cancelQueue() {
        return new Queue(QUEUE_CANCEL, true);
    }

    @Bean
    public Queue checkinQueue() {
        return new Queue(QUEUE_CHECKIN, true);
    }

    @Bean
    public Queue dlxQueue() {
        return new Queue(QUEUE_DLX, true);
    }

    @Bean
    public Binding timeoutBinding() {
        return BindingBuilder.bind(timeoutQueue())
                .to(reserveExchange())
                .with(RK_TIMEOUT);
    }

    @Bean
    public Binding cancelBinding() {
        return BindingBuilder.bind(cancelQueue())
                .to(reserveExchange())
                .with(RK_CANCEL);
    }

    @Bean
    public Binding checkinBinding() {
        return BindingBuilder.bind(checkinQueue())
                .to(checkinExchange())
                .with(RK_CHECKIN);
    }

    @Bean
    public Binding dlxBinding() {
        return BindingBuilder.bind(dlxQueue())
                .to(reserveExchange())
                .with(RK_DLX);
    }
}
