package demo.queue.confirm;

import com.rabbitmq.client.*;
import demo.queue.util.WindowHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


public class AsynchronousPublishConfirm {

    private static final Logger log = Logger.getLogger("Asynchronous.Publish.Confirm");
    private static final WindowHandler handler = new WindowHandler();

    static {
        log.setLevel(Level.ALL);
        log.setUseParentHandlers(true);
        handler.setLevel(Level.ALL);
        handler.setFormatter(new SimpleFormatter());
        log.addHandler(handler);
    }

    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("guest");
        factory.setPassword("guest");
        try(Connection connection = factory.newConnection();)
        {
            Channel channel = connection.createChannel();
            String routKey = "sch.queue";
            String message = "你好,世界";
            //开启确认模式
            channel.confirmSelect();
            channel.addConfirmListener(new ConfirmCallbackAckImplement(),new ConfirmCallbackNackImplement());

            AMQP.BasicProperties properties = new AMQP.BasicProperties().builder()
                    .contentEncoding("UTF-8")
                    .build();
            channel.basicPublish("",routKey,properties,message.getBytes(StandardCharsets.UTF_8));
            log.info("【x】 发送信息："+message);
            Thread.sleep(2000L);
            channel.close();
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static class ConfirmCallbackAckImplement implements ConfirmCallback {

        @Override
        public void handle(long l, boolean b) throws IOException {
            log.info("发送成功");
        }
    }

    public static class ConfirmCallbackNackImplement implements ConfirmCallback {

        @Override
        public void handle(long l, boolean b) throws IOException {
            log.severe("发送失败");
        }
    }

    private static class ConfirmListenerImplement implements ConfirmListener {

        @Override
        public void handleAck(long l, boolean b) throws IOException {
            log.info("发送成功");
        }

        @Override
        public void handleNack(long l, boolean b) throws IOException {
            log.info("发送失败");
        }
    }
}
