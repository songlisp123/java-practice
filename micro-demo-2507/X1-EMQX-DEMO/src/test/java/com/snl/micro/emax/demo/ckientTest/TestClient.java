package com.snl.micro.emax.demo.ckientTest;

import com.alibaba.fastjson2.JSON;
import com.snl.micro.emqx.demo.EmqxApplcaition;
import com.snl.micro.emqx.demo.entity.User;
import com.snl.micro.emqx.demo.producer.MqttProducer;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.charset.StandardCharsets;

@SpringBootTest(classes = EmqxApplcaition.class)
public class TestClient {

    @Autowired
    private MqttClient mqttClient;

    @Autowired
    private MqttProducer producer;

    @Test
    void test() {
        String topic = "傻逼";
        MqttMessage mqttMessage = new MqttMessage();
        mqttMessage.setPayload("我的世界真好玩".getBytes(StandardCharsets.UTF_8));
        mqttMessage.setQos(0);
        mqttMessage.setRetained(true);
        mqttMessage.setId(10000);
        try {
            mqttClient.publish(topic,mqttMessage);
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void get() {
        MqttTopic topic = mqttClient.getTopic("傻逼");
        try {
            mqttClient.subscribe("傻逼");
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void publish() {
        //发送信息
        producer.doSend("测试","测试监听函数");
    }

    @Test
    void publishEntity() {
        //发送user数据
        User user = new User("snl",12,"男");
        String json;
        json = JSON.toJSON(user).toString();
        producer.doSend("测试",json);
    }

    @Test
    void subScrible() {
        //消费消息
    }
}
