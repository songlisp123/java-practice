package com.snl.micro.emqx.demo.client;

import com.snl.micro.emqx.demo.producer.MqttClientCallBack;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Slf4j
@Configuration(proxyBeanMethods = true)
public class EmqxClientConfiguration {

    @Value("${charging.emqx.address}")
    private String address;

    @Value("${charging.emqx.username}")
    private String username;

    @Value("${charging.emqx.password}")
    private String password;

    @Value("${charging.emqx.group}")
    private String group;

    @Autowired
    private MqttClientCallBack clientCallBack;

    @Bean
    public MqttClient createMqttClient() {
        MqttClient client = null;
        try {
            log.info("开始连接eqmx的日志");
            client = new MqttClient(address, UUID.randomUUID().toString().replace("-",""),new MemoryPersistence());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setUserName(username);
            options.setPassword(password.toCharArray());
            client.setCallback(clientCallBack);

            client.connect(options);
            log.info("连接成功");
            client.subscribe("傻逼");
            client.subscribe("$share/"+group+"/测试");
        } catch (Exception e) {
            log.debug("发生异常，{}",e.getMessage());
        }
        return client;
    }
}
