package demo.consumer;

import com.rabbitmq.client.Channel;
import demo.common.DelayName;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Slf4j
@Component
public class MessageConsumer {

    private final String DEAD_QUEUE = DelayName.DELAY_QUEUE.getName();

//    @RabbitListener(queues = "demo_queue")
//    public void consumer01(String message) {
//        log.info("【✅】 接受信息：{}",message);
//    }

//    /**
//     * 第二种方法接受信息对象
//     * @param message 信息对象
//     */
//    @RabbitListener(queues = "demo_queue")
//    public void consumer02(Message message) {
//        String body = new String(message.getBody(), StandardCharsets.UTF_8);
//        //拿到属性
//        MessageProperties messageProperties = message.getMessageProperties();
//        log.info("【✅】 接受信息：{}",message);
//    }

//    /**
//     * 第三种解餐方式
//     * @param channel 链接渠道
//     */
//    @RabbitListener(queues = "demo_queue")
//    public void consumer03(Channel channel) {
//        log.info("channel={}",channel);
//    }

    /**
     * 混合接受参数形式
     * @param msg 反序列化后的信息体
     * @param message 封装的信息对象，包含{@code byte}字节数组的信息体和{@code properties}的消息属性
     * @param channel 消息传递的渠道
     */
    @RabbitListener(queues = "dlx_queue")
    public void consumerComposing(String msg,Message message,Channel channel) {
        log.info("当前时间:"+ LocalDateTime.now());
        log.info("接受信息：{}",msg);
        log.info("接受信息封装对象：{}",message);
        log.info("链接渠道：{}",channel);
    }


}
