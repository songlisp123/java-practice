package demo.producer;


import com.rabbitmq.stream.ByteCapacity;
import com.rabbitmq.stream.Environment;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Producer {
    public static void main(String[] args) {
        try{
            Environment environment = Environment.builder()
                    .build();
            String stream = "hello-java-stream";
            environment.streamCreator().
                    stream(stream).
                    maxLengthBytes(ByteCapacity.GB(5)).create();
            com.rabbitmq.stream.Producer producer = environment.producerBuilder().stream(stream).build();
            producer.send(producer
                    .messageBuilder()
                    .addData("毙了，这傻逼的流".getBytes())
                    .build(), null);
            System.out.println("发送成功");
            System.out.println("按键退出");
            System.in.read();
            producer.close();;
            environment.close();
//            producer.close();
//            environment.close();
        }
        catch (Exception e) {
            log.info("发生异常,{}",e.getMessage());
        }
    }
}
