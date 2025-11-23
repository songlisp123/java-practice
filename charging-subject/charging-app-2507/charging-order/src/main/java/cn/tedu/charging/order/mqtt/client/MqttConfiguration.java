package cn.tedu.charging.order.mqtt.client;

import cn.tedu.charging.common.constant.MqttTopicConst;
import cn.tedu.charging.order.mqtt.callBack.MqttCallBack;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Slf4j
@Configuration
public class MqttConfiguration {

    @Value("${charging.emqx.address}")
    private String address;

    @Value("${charging.emqx.username}")
    private String username;

    @Value("${charging.emqx.password}")
    private String password;

    @Autowired
    private MqttCallBack mqttCallBack;

    @Bean
    public MqttClient CreateClient() {
        MqttClient client = null;
        try {
            log.debug("开始创建客户端");
            client = new MqttClient(address, UUID.randomUUID().toString(),new MemoryPersistence());
            MqttConnectOptions options = new MqttConnectOptions();
            //设置用户名称
            options.setUserName(username);
            //设置用户密码
            options.setPassword(password.toCharArray());
            //设置自动重新连接客户端的默认操作
            options.setAutomaticReconnect(true);
            //设置清除session操作
            options.setCleanSession(true);
            //设置连接超时限制
            options.setConnectionTimeout(60);
            //设置心跳间隔时间
            options.setKeepAliveInterval(30);
            client.setCallback(mqttCallBack);
            client.connect(options);
            //订阅主题
            client.subscribe("$share/order/"+ MqttTopicConst.GUN_CHECK_RESULT_TOPIC);
            client.subscribe("consumer");
            log.debug("客户端创建成功");
        } catch (MqttException e) {
            log.debug("创建客户端失败，失败原因;{}",e.getMessage());
        }
        return client;
    }


}
