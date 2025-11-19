package demo.queue.emitLogsConsumer;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class EmitLogsTopicConsumer04 {
    private static final String EX_CHANGE = "topic_exchange_logs_test";

    private static final String[] bindingKeys = new String[]{
            "sys.*","*.info","sys.error","#"
    };

    public static void main(String[] args) throws IOException, TimeoutException {
        //创建
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        //获取管道
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EX_CHANGE, BuiltinExchangeType.TOPIC);

        //获取服务器生成的队列名称
        String queueName = channel.queueDeclare().getQueue();
        //绑定
        String bindingKey  = "#";

        channel.queueBind(queueName,EX_CHANGE,bindingKey);
        System.out.println("【*】 等待信息。退出请按【ctrl+c】");
        System.out.println("队列绑定路由键:"+bindingKey);

        boolean ack = true;
        channel.basicConsume(queueName,ack,(csg,delivery)->{
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            //获取路由键
            String routingKey = delivery.getEnvelope().getRoutingKey();
            System.out.println("【√】接受信息："
                    +delivery.getEnvelope().getRoutingKey()+" :"+ message);
        },
                tag->{});
    }
}
