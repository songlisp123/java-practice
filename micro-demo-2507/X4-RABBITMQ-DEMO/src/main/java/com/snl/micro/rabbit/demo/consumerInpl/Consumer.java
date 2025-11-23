package com.snl.micro.rabbit.demo.consumerInpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Consumer {
    @RabbitListener(queues = "demo_queue")
    public void consumer01 (String message) {
        //message封住的消息提
        log.info("message={}", message);

    }
}
