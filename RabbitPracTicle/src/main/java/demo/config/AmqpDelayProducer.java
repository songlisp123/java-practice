package demo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.MessagePropertiesBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class AmqpDelayProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendDelay(String exChange,String routeKey,String message,Integer delayTime) {
        //获取消息体
        byte[] bytes = message.getBytes(StandardCharsets.UTF_8);
        //获取消息属性
        MessageProperties properties = MessagePropertiesBuilder.newInstance()
                .setContentType("text/plain")
                .setContentEncoding("UTF-8")
                .setExpiration(delayTime.toString())
                .build();
        Message send = new Message(bytes,properties);
        rabbitTemplate.send(exChange,routeKey,send);
    }
}
