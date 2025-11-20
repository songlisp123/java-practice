package demo.queue.deadLineImplement;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class DeadTestDemo {

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
            //死信交换机
            channel.exchangeDeclare(DLX_EX, BuiltinExchangeType.DIRECT);
            //死信队列
            channel.queueDeclare(DLX_Q,false,false,false,null);
            //绑定死信
            channel.queueBind(DLX_Q,DLX_EX,DLX_RK);
            //申明业务交换机
            channel.exchangeDeclare(BIZ_EX,BuiltinExchangeType.FANOUT);
            Map<String,Object> properties = new HashMap<>();
            properties.put("x-dead-letter-exchange",DLX_EX);
            properties.put("x-dead-letter-routing-key",DLX_RK);
            channel.queueDeclare(BIZ_Q,false,false,false,properties);
            channel.queueBind(BIZ_Q,BIZ_EX,"");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}
