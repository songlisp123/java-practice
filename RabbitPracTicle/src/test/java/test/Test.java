package test;

import com.rabbitmq.stream.ByteCapacity;
import com.rabbitmq.stream.Environment;
import com.rabbitmq.stream.Producer;
import demo.RabbitApplication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest(classes = RabbitApplication.class)
public class Test {

    @org.junit.jupiter.api.Test
    void test() {
        try{
            Environment environment = Environment.builder()
                            .host("localhost")
                    .build();
            String stream = "hello-java-stream";
            environment.streamCreator().stream(stream).maxLengthBytes(ByteCapacity.GB(5)).create();
            Producer producer = environment.producerBuilder().stream(stream).build();
            producer.send(producer
                    .messageBuilder()
                    .addData("xxxxxx".getBytes())
                    .build(), null);
            System.out.println("发送成功");
            producer.close();
            environment.close();
        }
        catch (Exception e) {
            log.info("发生异常,{}",e.getMessage());
        }
    }
}
