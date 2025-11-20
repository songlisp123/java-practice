package demo.queue.deadLineImplement.consumer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.concurrent.TimeoutException;

public class DeadMessageConsumer {
    /**
     * 参考约定名称和绑定关系 声明一套死信的组件结构
     */
    private static final String BIZ_EX="biz_ex";
    private static final String BIZ_Q="biz_q";
    private static final String DLX_EX="dlx_ex";
    private static final String DLX_Q="dlx_q";
    private static final String DLX_RK="dlx_rk";
    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("guest");
        factory.setPassword("guest");
        try(Connection connection = factory.newConnection();) {
            Channel channel = connection.createChannel();

            channel.basicConsume(DLX_Q,false,(cmt,delivery)->{
                //拒绝接收信息
//                channel.basicNack(delivery.getEnvelope().getDeliveryTag(),false,false);
                String body = new String(delivery.getBody(), StandardCharsets.UTF_8);
                        System.out.println("当前时间"+LocalDateTime.now());
                        System.out.println("接收信息："+body);
                        channel.basicAck(delivery.getEnvelope().getDeliveryTag(),true);
            },
                    cmt->{});
            Thread.sleep(100000L);
        } catch (IOException | InterruptedException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}
