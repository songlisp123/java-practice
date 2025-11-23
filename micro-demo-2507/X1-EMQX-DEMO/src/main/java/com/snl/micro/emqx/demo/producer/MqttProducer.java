package com.snl.micro.emqx.demo.producer;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Slf4j
@Component
public class MqttProducer {

    @Autowired
    private MqttClient mqttClient;

    /**
     * 使用mqtt协议发送信息
     * @param topic 信息主题
     * @param message 信息详情
     * @param ops 信息级别
     * @param msgId 信息编号
     * @param retained 信息是否保留，{@code true}信息将会保留<br/>
     * @return {@code true} 发送成功<br/>
     * {@code false} 发送失败
     */
    private boolean doSend(String topic,String message,int ops,Integer msgId,boolean retained) {
        //获取信息对象
        try{
            log.info("开始发送信息");
            MqttMessage mqttMessage = new MqttMessage(message.getBytes(StandardCharsets.UTF_8));
            mqttMessage.setRetained(retained);
            mqttMessage.setQos(ops);
            if (!Objects.isNull(msgId)) {
                mqttMessage.setId(msgId);
            }
            this.mqttClient.publish(topic,mqttMessage);
            log.info("信息发送成功");
            return true;
        }catch (Exception e) {
            log.debug("信息发送失败",e);
            return false;
        }
    }

    /**
     * 封装mqtt协议发送信息代码
     * @param topic 信息主题
     * @param message 信息详情
     * @return {@code true} 发送成功<br/>
     * {@code false} 发送失败
     */
    public boolean doSend(String topic, String message) {
        return this.doSend(topic,message,0,null,true);
    }
}
