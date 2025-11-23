package cn.tedu.charging.order.AMQP;

import cn.tedu.charging.common.pojo.message.DelayCheckMessage;
import cn.tedu.charging.order.service.ConsumerService;
import com.alibaba.fastjson2.JSON;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class AmqpConsumer {

    @Autowired
    private ConsumerService consumerService;

    @Autowired
    private RedisTemplate redisTemplate;

    //TODO 消费逻辑 【✅  完成】
//    @RabbitListener(queues = "dlx_queue")
//    public void consume(String message) {
//        log.info("接受信息:{}",message);
//    }

    @RabbitListener(queues = "dlx_queue")
    public void consumeDelay(String json, Message message, Channel channel) {
        log.info("接受信息:{}",json);
        //1、解析参数
        DelayCheckMessage delayCheckMessage = JSON.parseObject(json, DelayCheckMessage.class);
        //拿到信息属性
        MessageProperties messageProperties = message.getMessageProperties();
        //TODO 添加锁 【✅  完成】
        ValueOperations operations = redisTemplate.opsForValue();
        String lockingKey = "charge:order:consume:lock:"+delayCheckMessage.getOrderNo();
        Boolean hasLocked = false;
        int count = 0;
        try {
            do {
                if (count == 0 ) {
                    log.info("第一次抢锁，直接抢");
                    hasLocked = operations.setIfAbsent(lockingKey,"",5, TimeUnit.SECONDS);
                } else if (count < 4)  {
                    log.info("第{}次抢锁",count+1);
                    Thread.sleep(200);
                    hasLocked = operations.setIfAbsent(lockingKey,"",5, TimeUnit.SECONDS);
                }
                else {
                    log.error("抢锁失败");
                    break;
                }
                count++;
            } while (!hasLocked);
            //TODO 抢到锁调用消费逻辑【✅  完成】
            consumerService.handleCheckNoRes(delayCheckMessage);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            redisTemplate.delete(lockingKey);
        }
        //3\手动确认
        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        } catch (IOException e) {
            log.error("消费失败，失败原因:{}",e.getMessage());
        }

    }

}
