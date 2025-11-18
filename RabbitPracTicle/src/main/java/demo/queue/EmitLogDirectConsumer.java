package demo.queue;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileAttribute;
import java.util.concurrent.TimeoutException;

public class EmitLogDirectConsumer {
    private static final String EX_CHANGE = "direct_logs";

    private static final String[] severity = new String[]{"info","warning","error"};

    public static void main(String[] args) throws IOException, TimeoutException {
        Path rootPath = getRunPath();
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        //获取管道
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EX_CHANGE, BuiltinExchangeType.DIRECT);
        //获取队列
        String queue = channel.queueDeclare().getQueue();

        for (String s : severity) {
            channel.queueBind(queue,EX_CHANGE,s);
        }

//        channel.queueBind(queue,EX_CHANGE,"error");

        System.out.println("【*】 等待信息。退出请按【ctrl+c】");

        DeliverCallback callback = (cxt,delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            //获取路由键
            String routingKey = delivery.getEnvelope().getRoutingKey();
            System.out.println("【√】接受信息："
                    +delivery.getEnvelope().getRoutingKey()+" :"+ message);
            //TODO 打印错误信息到文件中
            //获取当前路径
            new Thread(write(message,rootPath,routingKey),"线程").start();

        };
        boolean ack = true;
        channel.basicConsume(queue,ack,callback,Tag->{});
    }

    private static Runnable write(String message,Path rootPath,String routeKey)  {
        return ()->{
            Path path = Path.of(rootPath.toString(),"error.log");
//            System.out.println(path);
            try {
                if (!path.toFile().exists()) {
                    Files.createFile(path);
                }
                String log = "["+routeKey+"]:" + message + "\n";
                Files.writeString(path, log, StandardOpenOption.APPEND);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }

    private static Path getRunPath() {
        String pathName = System.getProperty("user.dir");
        return Path.of(pathName);
    }
}
