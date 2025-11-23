package com.snl.micro.emqx.demo.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Slf4j
@Component
public class MqttClientCallBack implements MqttCallbackExtended {
    @Override
    public void connectionLost(Throwable throwable) {
        //连接断开
        log.info("连接失败，失败信息：{}",throwable.getMessage());
    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        log.info("主题是:{}",s);
        log.info("接收信息：{}", new String(mqttMessage.getPayload(), StandardCharsets.UTF_8));
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        //获取消息传达的token
        boolean complete = iMqttDeliveryToken.isComplete();
        //获取异常信息
        MqttException exception = iMqttDeliveryToken.getException();
        //判断服务器是否接受
        if (complete&& Objects.isNull(exception)) {
            //日过服务器接收到信息并且无任何抛出异常
            log.info("服务器接收信息成功");
        } else if (Objects.nonNull(exception)) {
            //服务器接收到信息但是分析信息的时候抛出异常
            log.error("信息处理失败");
        }else {
            //发送失败
            log.error("发送失败");
        }
    }

    @Override
    public void connectComplete(boolean reconnect, String serverUrl) {
        if (reconnect) {
            log.info("连接服务器成功，url={}",serverUrl);

        }else  {
            log.info("首次链接成功，url={}",serverUrl);
        }
    }
}
