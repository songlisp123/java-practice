package test;

import com.rabbitmq.stream.ByteCapacity;
import com.rabbitmq.stream.Environment;
import com.rabbitmq.stream.Producer;
import com.snl.micro.rabbit.demo.RabbitApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest(classes = RabbitApplication.class)
public class producerTest {

    @Test
    void send() {
       try( Environment environment = Environment.builder().build()) {
           String stream = "hello-java-stream";
           environment.streamCreator().stream(stream).maxLengthBytes(ByteCapacity.GB(5)).create();
           Producer producer = environment.producerBuilder().stream(stream).build();
           producer.send(producer
                   .messageBuilder()
                   .addData("我草".getBytes())
                   .build(), null);
           System.out.println(" [x] 'Hello, World!' message sent");
       }
       catch (Exception e) {
           log.info("发生异常,{}",e.getMessage());
       }

    }
}
