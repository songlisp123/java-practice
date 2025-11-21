package demo.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MessageConsumer {

    @RabbitListener(queues = "demo_queue")
    public void consumer01(String message) {
        log.info("【✅】 接受信息：{}",message);
    }
}
