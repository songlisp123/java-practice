package demo.queue;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class EmitLogTopic {

    private static final String EX_CHANGE = "topic_exchange_logs_test";

    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try(Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();)
        {
            channel.exchangeDeclare(EX_CHANGE, BuiltinExchangeType.TOPIC);
            String message = "你好时节";
            String routeKey = "sys.error.info";
            channel.basicPublish(EX_CHANGE,routeKey,true,null,message.getBytes(StandardCharsets.UTF_8));
            System.out.println("【*】发送信息："+message);
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}
