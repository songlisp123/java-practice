package com.snl.micro.rabbit.demo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Slf4j
@Configuration
public class DeclareConfiguration {

    @Bean
    public Exchange createExchange() {
        return ExchangeBuilder.directExchange("direct_demo_ex").build();
    }

    @Bean
    public Queue createQueue() {
        return QueueBuilder.nonDurable("demo_queue").build();
    }

    @Bean
    public Binding bind() {
        return BindingBuilder.bind(createQueue()).to(createExchange()).with("demo_rk").noargs();
    }
}
