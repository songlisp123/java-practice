package com.snl.x3.rabbit.demo;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

@Slf4j
/**
 * 创建延迟信息处理
 */
public class RabbitChanelManger {

    public Channel createChannel() {
        try {
            //创建链接工厂
            log.debug("客户端链接rabiit服务端开始");
            ConnectionFactory factory = new ConnectionFactory();
            //设置url
            factory.setHost("localhost");
            //设置端口
            factory.setPort(5672);
            //设置用户
            factory.setUsername("guest");
            //设置密码
            factory.setPassword("guest");
            //创建链接
            Connection connection = factory.newConnection();
            log.debug("建立长连接成功");
            //获取通信管道
            log.debug("获取通信管道");
            Channel channel = connection.createChannel();
            log.debug("获取通信管道成功");
            return channel;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}
