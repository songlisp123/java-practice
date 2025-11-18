package demo.queue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class Rec {

    private static final String QUEUE_NAME = "hello";
    public static void main(String[] args) {
        //创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //创建链接
        factory.setHost("localhost");
        try {
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.queueDeclare(QUEUE_NAME,false,false,false,null);
            System.out.println("{^}正在接受信息，退出请按crtl+c");
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                System.out.println(" [x] 接收 '" + message + "'");
            };
            channel.basicConsume(QUEUE_NAME,true,deliverCallback,consumerTag->{});
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}
