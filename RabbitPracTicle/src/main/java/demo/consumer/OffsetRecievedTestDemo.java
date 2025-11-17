package demo.consumer;

import com.rabbitmq.stream.ByteCapacity;
import com.rabbitmq.stream.Environment;
import com.rabbitmq.stream.OffsetSpecification;

import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class OffsetRecievedTestDemo {
    public static void main(String[] args) {
        try(Environment environment = Environment.builder().build()) {
            String stream = "java-tracking-stream";
            environment.streamCreator().stream(stream).maxLengthBytes(ByteCapacity.GB(1L)).create();

            //接受信息
            //获取流操作选项
//            OffsetSpecification offsetSpecification = OffsetSpecification.first();
//            OffsetSpecification offsetSpecification = OffsetSpecification.offset(56);
            OffsetSpecification offsetSpecification = OffsetSpecification.next();
            AtomicLong firstOffset = new AtomicLong(-1);
            AtomicLong secondOffset = new AtomicLong(0);
            AtomicLong messageCount = new AtomicLong(0);
            CountDownLatch countDownLatch = new CountDownLatch(1);
            environment.consumerBuilder()
                    .stream(stream)
                    .offset(offsetSpecification)
                    .messageHandler(((context, message) -> {
                        if (firstOffset.compareAndSet(-1, context.offset())) {
                            System.out.println("接受第一条信息");
                        }
//                        System.out.println(message);
                        String body = new String(message.getBodyAsBinary(), StandardCharsets.UTF_8);
//                        System.out.println(body);
                        if (Objects.equals("mark",body)) {
                            secondOffset.set(context.offset());
                            context.consumer().close();
                            countDownLatch.countDown();
                        }
                    })).build();
            boolean running = true;
            Scanner scanner = new Scanner(System.in);
            while (running) {
                System.out.println("请按键退出：[y|Y]");
                if (Objects.equals(scanner.next(),"Y".toLowerCase(Locale.ROOT))) {
                    running =false;
                }
                System.out.println("开始消费……");
                boolean completed = countDownLatch.await(60, TimeUnit.SECONDS);

                System.out.printf("Done consuming, first offset %d, last offset %d.%n",
                        firstOffset.get(), secondOffset.get());

            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
