package demo.queue;


import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;

/**
 * 演示信息从生产者发送到交换机的例子
 */
public class EmitLog {
    /**
     * 交换机名称
     */
    private static final String EX_CHANGE = "log";
    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try(Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();)
        {
            //创建管道
            /**
             * 参数解释：
             * 第一个参数：交换机名称
             * 第二个参数：交换机类型
             */
            channel.exchangeDeclare(EX_CHANGE, BuiltinExchangeType.FANOUT);
            //信息
            String message = "我香炉管";
            channel.basicPublish(EX_CHANGE,"",null,message.getBytes(StandardCharsets.UTF_8));
            System.out.println("【x】 发送信息："+message);
        }catch (Exception e) {
//            log.error("创建连接失败,{}",e.getMessage());
        }
    }
}
