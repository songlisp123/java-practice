package demo.queue;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.concurrent.*;

/**
 * 使用direct直流交换机绑定信息
 */
public class EmitLogDirect {
    private static final String EX_CHANGE = "direct_logs";
    private static final String[] severity = new String[]{"info","warning","error"};

    private static final ScheduledExecutorService scheduledExecutorService =
            Executors.newScheduledThreadPool(10);
    public static void main(String[] args) {
        scheduledExecutorService.scheduleAtFixedRate(doWork(),1000L,10000L, TimeUnit.MILLISECONDS);
    }

    private static Runnable doWork() {
        return () -> {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            try(Connection connection = factory.newConnection();
                Channel channel = connection.createChannel();)
            {
                channel.exchangeDeclare(EX_CHANGE, BuiltinExchangeType.DIRECT);


                /**
                 * 第二个参数：路由键
                 */
                //随机发送不同信息
                int generate = generate();
                String message = "这是一条%s信息".formatted(severity[generate]);
                channel.basicPublish(EX_CHANGE,severity[generate], null,message.getBytes(StandardCharsets.UTF_8));
                System.out.println("【*】发送信息："+message);
            } catch (IOException | TimeoutException e) {
                throw new RuntimeException(e);
            }
        };
    }

    private static int generate() {
        int length = severity.length;
        return (int) (Math.random() * length);
    }
}
