package com.snl.micro.rabbit.demo.controler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilderSupport;
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

    @GetMapping("send")
    public String send(String message) {
        log.info("发送消息");
        //获取消息体
        byte[] bytes = message.getBytes(StandardCharsets.UTF_8);
        //获取消息属性
        MessageProperties build = MessagePropertiesBuilder.newInstance().setPriority(1000).build();

        //封装消息
        Message sendMessage = new Message(bytes,build);
        //发送消息
        rabbitTemplate.send("direct_demo_ex","demo_rk",sendMessage);
        return "[✅] 发送成功";
    }
}
