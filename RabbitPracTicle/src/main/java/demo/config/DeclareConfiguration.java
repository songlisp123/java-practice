package demo.config;

import demo.common.DelayName;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class DeclareConfiguration {

    @Bean
    public Exchange createExchange() {
        return ExchangeBuilder.directExchange(DelayName.DELAY_EX_CHANGE.getName()).build();
    }

    @Bean
    public Queue createQueue() {
        return QueueBuilder
                .durable(DelayName.DELAY_QUEUE.getName())
                .deadLetterExchange(DelayName.DEAD_LETTER_EX_CHANGE.getName())
                .deadLetterRoutingKey(DelayName.DEAD_ROUT_KEY.getName())
                .build();
    }

    @Bean
    public Binding bind() {
        return BindingBuilder.
                bind(createQueue())
                .to(createExchange())
                .with(DelayName.DELAY_BINGING_KEY.getName())
                .noargs();
    }

    @Bean
    public Exchange createDeadExchange() {
        return ExchangeBuilder.fanoutExchange(DelayName.DEAD_LETTER_EX_CHANGE.getName()).build();
    }

    @Bean
    public Queue createDeadQueue() {
        return QueueBuilder.nonDurable(DelayName.DEAD_LETTER_QUEUE.getName()).build();
    }

    @Bean
    public Binding createBinding() {
        return BindingBuilder.bind(createDeadQueue())
                .to(createDeadExchange())
                .with(DelayName.DEAD_BINGING_KEY.getName())
                .noargs();
    }
}
