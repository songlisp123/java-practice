package demo.queue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class NewTask {
    private static final String TASK_QUEUE_NAME = "task_queue";
    public static void main(String[] args) {
        //创建rabbit链接工厂
        ConnectionFactory factory  = new ConnectionFactory();
        factory.setHost("localhost");
        //与代理服务器建立连接
        try(Connection connection = factory.newConnection();
            Channel channel = connection.createChannel())
        {
            boolean durable = false;
            channel.queueDeclare(TASK_QUEUE_NAME,durable,false,false,null);
            String message = "play";
            /**
             * basciPublish参数分析：
             * 第一个参数：交换机名称，默认是无名的交换机
             * 第二个参数：将要发送到的队列名称
             * 第三个参数：信息的属性
             * 第四个参数：信息字节流
             */
            channel.basicPublish("",TASK_QUEUE_NAME,
                    MessageProperties.PERSISTENT_TEXT_PLAIN, //修改此行,确保该队列的信息也会持久化
                    message.getBytes(StandardCharsets.UTF_8));
            System.out.println("[x] 已发送"+message);
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

}
