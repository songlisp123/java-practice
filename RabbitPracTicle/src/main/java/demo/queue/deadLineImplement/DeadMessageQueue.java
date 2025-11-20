package demo.queue.deadLineImplement;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class DeadMessageQueue {

    private static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("guest");
        factory.setPassword("guest");
        try(Connection connection = factory.newConnection();) {
            Channel channel = connection.createChannel();
            Map<String,Object> properties = new HashMap<>();
            properties.put("x-expires",100000L);
            properties.put("x-message-ttl",10000L);
            channel.queueDeclare(DEAD_QUEUE,false,false,false,properties);

            String message = "你好，世界";
            publish(channel,message);
            System.out.println("发送信息"+message);
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    private static void publish(Channel channel,String message) throws IOException {
        channel.basicPublish("",DEAD_QUEUE,null,message.getBytes(StandardCharsets.UTF_8));
    }
}
