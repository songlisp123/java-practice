package demo.queue.deadLineImplement.publisher;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class DeadMessageTest {
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

//            channel.exchangeDeclare(BIZ_EX, BuiltinExchangeType.FANOUT);
//            channel.queueDeclare(BIZ_Q,false,false,false,null);
            String message = LocalDateTime.now() +"";
            Map<String,Object> headers = new HashMap<>();
//            headers.put("expiration",10000L);
//            AMQP.BasicProperties properties = new AMQP.BasicProperties()
//                    .builder()
//                    .expiration("6000") //消息延迟一分钟
//                    .build();
            AMQP.BasicProperties.Builder builder = new AMQP.BasicProperties.Builder();
            builder.expiration("60000");//消息会延迟6s
            AMQP.BasicProperties properties = builder.build();
            channel.basicPublish(BIZ_EX,"",properties,message.getBytes(StandardCharsets.UTF_8));
            System.out.println("放松信息成功："+message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}
