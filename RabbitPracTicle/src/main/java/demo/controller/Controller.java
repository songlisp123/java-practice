package demo.controller;


import demo.common.DelayName;
import demo.config.AmqpDelayProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.MessagePropertiesBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;

@Slf4j
@RestController
public class Controller {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private AmqpDelayProducer producer;

    @GetMapping("send")
    public String send(String message) {
        log.info("[*] 发送信息：{}",message);
        byte[] bytes = message.getBytes(StandardCharsets.UTF_8);
        MessageProperties properties = MessagePropertiesBuilder.newInstance()
                .setContentType("text/plain")
                .setContentEncoding("UTF-8")
                .setPriority(1000)
                .setExpiration("60000")
                .build();
        Message sending = new Message(bytes,properties);
        rabbitTemplate.send("demo_exchange","demo_rk",sending);
        log.info("【*】 信息发送成功");
        return "[ok]";
    }

    @GetMapping("send/delay")
    public String sendDelay(String message) {
        producer.sendDelay(
                DelayName.DELAY_EX_CHANGE.getName(),
                DelayName.DELAY_BINGING_KEY.getName(),
                message,60000);
        return "OK!";
    }
}
