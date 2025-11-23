package order.test.mqttTest;

import cn.tedu.charging.order.OrderApp;
import cn.tedu.charging.order.mqtt.producer.MqttProducer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = OrderApp.class)
public class MqttTest {

    @Autowired
    private MqttProducer producer;

    private  final String topic = "snl";
    private  final String message = "这是一条新信息";

    @Test
    void test() {
        //测试生产者发送信息
        producer.doSend(topic,message);
    }
}
