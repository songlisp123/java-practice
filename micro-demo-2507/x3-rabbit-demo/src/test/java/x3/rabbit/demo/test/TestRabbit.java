package x3.rabbit.demo.test;

import com.rabbitmq.client.*;
import com.snl.x3.rabbit.demo.RabbitApplication;
import com.snl.x3.rabbit.demo.RabbitChanelManger;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

@Slf4j
@SpringBootTest(classes = RabbitApplication.class)
public class TestRabbit {
//
//    private final RabbitChanelManger manger = new RabbitChanelManger();
//    private Channel channel  = manger.createChannel();

    @Test
    void test() {
        RabbitChanelManger manger = new RabbitChanelManger();
    }

    @Test
    void createQueue() {
        //获取长连接
        RabbitChanelManger manger = new RabbitChanelManger();
        Channel channel = manger.createChannel();
        try {
            AMQP.Queue.DeclareOk queue01 = channel.queueDeclare(
                    "队列02",
                    false,
                    false,
                    false,
                    null
            );
            log.info("声明一个队列成功");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void addMessage() {
        //发送信息
        RabbitChanelManger manger = new RabbitChanelManger();
        Channel channel = manger.createChannel();
        //消息体
        byte[] body = "这是第二条信息".getBytes(StandardCharsets.UTF_8);
        //设置消息属性
        AMQP.BasicProperties.Builder builder = new AMQP.BasicProperties.Builder();
        builder.contentEncoding("utf-8".toUpperCase(Locale.ROOT));
        builder.contentType("text/plain");
        //自定义属性
        Map<String,Object> headers = new HashMap<>();
        headers.put("name","snl");
        headers.put("age",25);
        builder.headers(headers);
        //使用builder获取最终属性对象?这一步是什么意思?
        AMQP.BasicProperties properties = builder.build();
        //消息路由
        String router ="队列02";
        //3.调用api发送消息
        //3.1任何生产者都要将消息发送到交换机 第一个参数是一个交换机名称exchange
        try {
            channel.basicPublish("",router,properties,body);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void consume() {
        //获取信息
        RabbitChanelManger manger = new RabbitChanelManger();
        try{
            Channel channel = manger.createChannel();
            ConsumerImplement consumerImplement = new ConsumerImplement();
            channel.basicConsume("队列02", consumerImplement);
//            channel.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private class ConsumerImplement implements Consumer {

        /**
         * 信息发送后调用的方法,这个方法只执行一次,是从队列中拉出信息
         * @param s 一个可变的标识符
         */
        @Override
        public void handleConsumeOk(String s) {
            log.info("1");
        }

        @Override
        public void handleCancelOk(String s) {
            log.info("2");
        }

        @Override
        public void handleCancel(String s) throws IOException {
            log.info("3");
        }

        /**
         * 关闭链接管道后,处理的方法
         * @param s 当前信息标识符
         * @param e 受检异常
         */
        @Override
        public void handleShutdownSignal(String s, ShutdownSignalException e) {
            log.info("4");
            if (Objects.nonNull(e)) {
                log.info("异常是={}", e.getMessage());
            }

        }

        /**
         * 处理回复操作
         * @param s 当前信息的标识符
         */
        @Override
        public void handleRecoverOk(String s) {
            log.info("5");
        }

        /**
         * 核心业务,消费者接受调用次函数
         * @param s 当前信息的标识符
         * @param envelope 封装的信息数据
         * @param basicProperties 发送的信息基础属性
         * @param bytes 信息体
         * @throws IOException 抛出io异常
         */
        @Override
        public void handleDelivery(String s, Envelope envelope, AMQP.BasicProperties basicProperties, byte[] bytes) throws IOException {
            log.info("6");
            log.info("接受信息成功");
            String body = new String(bytes,StandardCharsets.UTF_8);
            log.info("消息体:{}",body);
            log.info("当前标签是:{}",envelope.getDeliveryTag());
        }
    }
}
