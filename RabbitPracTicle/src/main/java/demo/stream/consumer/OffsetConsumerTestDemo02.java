package demo.stream.consumer;

import com.rabbitmq.stream.ByteCapacity;
import com.rabbitmq.stream.Environment;
import com.rabbitmq.stream.OffsetSpecification;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class OffsetConsumerTestDemo02 {
    public static void main(String[] args) {
        try(Environment environment = Environment.builder().build()) {
            String stream = "java-tracking-stream";
            environment.streamCreator().stream(stream).maxLengthBytes(ByteCapacity.GB(1L)).create();

            //创建流偏移
            OffsetSpecification offsetSpecification = OffsetSpecification.first();
            AtomicLong firstOffset = new AtomicLong(-1);
            AtomicLong secondOffset = new AtomicLong(0);
            AtomicLong messageCount = new AtomicLong(0);
            CountDownLatch countDownLatch = new CountDownLatch(1);
            //变化在这里
            environment.consumerBuilder()
                    .stream(stream)
                    .offset(offsetSpecification)
                    .name("tracking-stream") //变化1
                    .manualTrackingStrategy().builder() //变化2
                    .messageHandler(((context, message) -> {
                        String body = new String(message.getBodyAsBinary(), StandardCharsets.UTF_8);
                        if (firstOffset.compareAndSet(-1, context.offset())) {
                            System.out.println("接受第一条信息");
                            System.out.println("第一条信息是:"+body);
                        }
                        if (messageCount.incrementAndGet() % 10 == 0) { //变化3
                            context.storeOffset();
                        }
                        if (Objects.equals("mark",body)) {
                            secondOffset.set(context.offset());
                            context.storeOffset();
                            System.out.println("最后一条信息是:"+body);
                            context.consumer().close();
                            countDownLatch.countDown();
                        }
                    }))
                    .build();
            System.out.println("正在接受信息");
            boolean completed = countDownLatch.await(60, TimeUnit.SECONDS);
            System.out.println("消费信息："+completed);
            System.out.printf("Done consuming, first offset %d, last offset %d.%n",
                    firstOffset.get(), secondOffset.get());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
