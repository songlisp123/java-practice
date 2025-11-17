package demo.producer;


import com.rabbitmq.stream.ByteCapacity;
import com.rabbitmq.stream.Environment;
import com.rabbitmq.stream.Producer;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@Slf4j
public class OffsetTrackingTest {
    public static void main(String[] args) {
        //获取流
        try(Environment environment = Environment.builder().build()) {
            String stream = "java-tracking-stream";
            environment.streamCreator()
                    .stream(stream)
                    .maxLengthBytes(ByteCapacity.GB(1L))
                    .create();

            //生产者
            Producer producer = environment.producerBuilder().stream(stream).build();

            //发送信息数量
            int messageCount = 100;
            CountDownLatch countDownLatch = new CountDownLatch(messageCount);
            System.out.printf("发送 %d 条信息%n",messageCount);
            IntStream.range(0,messageCount).forEach(i -> {
                String body = (i == messageCount-1)?"mark":"hello";
                producer.send(producer.messageBuilder().addData(body.getBytes(StandardCharsets.UTF_8)).build(),
                        confirmationStatus -> {
                            if (confirmationStatus.isConfirmed()) {
                                countDownLatch.countDown();
                            }
                        });
            });
            boolean completed = countDownLatch.await(60, TimeUnit.SECONDS);
            System.out.printf("信息确认：%b.%n",completed);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
