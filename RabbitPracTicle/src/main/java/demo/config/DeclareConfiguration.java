package demo.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class DeclareConfiguration {

    @Bean
    public Exchange createExchange() {
        return ExchangeBuilder.directExchange("demo_exchange").build();
    }

    @Bean
    public Queue createQueue() {
        return QueueBuilder.durable("demo_queue").build();
    }

    @Bean
    public Binding bind() {
        return BindingBuilder.
                bind(createQueue()).to(createExchange()).
                with("demo_rk").
                noargs();
    }
}
