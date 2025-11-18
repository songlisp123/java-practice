package demo.queue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class Send {

    private static final String queueName = "hello";

    public static void main(String[] args) {
        //创建rabbit链接工厂
        ConnectionFactory factory  = new ConnectionFactory();
        factory.setHost("localhost");
        //与代理服务器建立连接
        try(Connection connection = factory.newConnection();
            Channel channel = connection.createChannel())
        {
            channel.queueDeclare(queueName,false,false,false,null);
            String message = "《计算机科学基础》";
            channel.basicPublish("",queueName,null,message.getBytes(StandardCharsets.UTF_8));
            System.out.println("[x] 已发送"+message);
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}
