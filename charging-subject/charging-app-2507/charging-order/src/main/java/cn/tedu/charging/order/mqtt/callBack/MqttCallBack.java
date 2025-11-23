package cn.tedu.charging.order.mqtt.callBack;

import cn.tedu.charging.common.constant.MqttTopicConst;
import cn.tedu.charging.common.pojo.message.CheckResultMessage;
import cn.tedu.charging.order.service.ConsumerService;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class MqttCallBack implements MqttCallbackExtended {

    @Autowired
    private ConsumerService consumerService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 此函数是mqtt连接服务端失败后调用
     * @param throwable 一个可抛出的受检异常
     */
    @Override
    public void connectionLost(Throwable throwable) {
        //连接断开
        log.debug("连接失败，失败信息：{}",throwable.getMessage());
    }

    /**
     * 这是一个客户端接受服务端的信息时候会调用的函数
     * @param s 信息主题
     * @param mqttMessage 信息封装数据
     * @throws Exception 如果发生任何异常，则抛出
     */
    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        //判断主题来源
        log.debug("主题是:{}",s);
        log.debug("接收信息：{}", new String(mqttMessage.getPayload(), StandardCharsets.UTF_8));
        if (Objects.nonNull(s) && Objects.equals(s, MqttTopicConst.GUN_CHECK_RESULT_TOPIC)) {
            //解析为json对象
            CheckResultMessage result = JSON.parseObject(mqttMessage.toString(), CheckResultMessage.class);
            log.debug("接收设备信息:{}",result);
            //TODO 添加锁 【✅  完成】
            ValueOperations operations = redisTemplate.opsForValue();
            String lockingKey = "charge:order:consume:lock:"+result.getOrderNo();
            Boolean hasLocked = false;
            int count = 0;
            try{
                do {
                    if (count == 0) {
                        log.info("第一次抢锁，直接抢");
                        hasLocked = operations.setIfAbsent(lockingKey,"",5, TimeUnit.SECONDS);
                    } else if (count < 4) {
                        log.info("第{}次抢锁",count+1);
                        Thread.sleep(200);
                        hasLocked = operations.setIfAbsent(lockingKey,"",5,TimeUnit.SECONDS);
                    }else {
                        log.error("抢锁失败");
                        break;
                    }
                    count++;
                }while (!hasLocked);
                //处理消费逻辑
                consumerService.handlerCheckResult(result);
            } catch (Exception e) {
                log.error("异常：{}",e.getMessage());
            }finally {
                redisTemplate.delete(lockingKey);
            }
        }
    }

    /**
     * 这是客户端向服务端发送信息会调用的函数
     * @param iMqttDeliveryToken 信息接受token
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        //获取消息传达的token
        boolean complete = iMqttDeliveryToken.isComplete();
        //获取异常信息
        MqttException exception = iMqttDeliveryToken.getException();
        //判断服务器是否接受
        if (complete&& Objects.isNull(exception)) {
            //日过服务器接收到信息并且无任何抛出异常
            log.debug("服务器接收信息成功");
        } else if (Objects.nonNull(exception)) {
            //服务器接收到信息但是分析信息的时候抛出异常
            log.debug("信息处理失败");
        }else {
            //发送失败
            log.debug("发送失败");
        }
    }

    /**
     * 这是客户端链接服务端会调用的函数
     * @param reconnect 是否重连
     * @param serverUrl 服务端url地址
     */
    @Override
    public void connectComplete(boolean reconnect, String serverUrl) {
        if (reconnect) {
            log.debug("连接服务器成功，url={}",serverUrl);
        }else  {
            log.debug("首次链接成功，url={}",serverUrl);
        }
    }
}
