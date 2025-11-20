package demo.queue.confirm;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class SchorizedMech {
    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("guest");
        factory.setPassword("guest");
        try(Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();)
        {
            String routKey = "sch.queue";
            String message = "你好,世界";
            //开启确认模式
            channel.confirmSelect();
            AMQP.BasicProperties properties = new AMQP.BasicProperties().builder()
                            .contentEncoding("UTF-8")
                            .contentType("text/plain")
                            .userId("12")
                            .build();
            channel.basicPublish("",routKey,properties,message.getBytes(StandardCharsets.UTF_8));
            System.out.println("【x】 发送信息："+message);
            boolean confirms = channel.waitForConfirms(5000L);
            if (confirms) {
                System.out.println("【✅】 代理接收消息成功");
            }else {
                System.out.println("【❌】 消息发送超时，未确认");
            }
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
