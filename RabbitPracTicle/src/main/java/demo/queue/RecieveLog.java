package demo.queue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class RecieveLog {

    private static final String EX_CHANGE = "log";

    public static void main(String[] args) throws IOException, TimeoutException {
        //获取消费者
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        //获取连接
        Connection connection = factory.newConnection();
        //获取管道
        Channel channel = connection.createChannel();
        //该队列每次只能接受两个未解封的信息
        /**
         * API解释：服务器将发送的最大信息数，如果无限制则为o
         */
        channel.basicQos(2);
        //声明队列，此队列将会与交换机绑定
        //空参数体代表着生成的是非持久化，发送参数为1且自动删除的队列
        String queueName = channel.queueDeclare().getQueue();
        //将交换机绑定到特定的队列上
        /**
         * 将队列绑定到交换机上
         * 参数解释：
         * 第一个参数：交换机名称
         * 第二个参数：队列名称
         * 第三个名称：绑定时使用的路由键，这个为什么为空？
         * 对于fanout交换机，这个参数会被自动忽略掉
         */
        channel.queueBind(queueName,EX_CHANGE,"");
        System.out.println("[*] 等待接受信息，请按【ctrl-c】停止程序");

        //设置回调函数
        DeliverCallback callback = (consumerTag,delivery)->{
            String body = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println("【√】接受信息体："+body);
        };

        boolean ack = true;
        channel.basicConsume(queueName,ack,callback,consumerTag->{});

    }
}
