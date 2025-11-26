package cn.tedu.charging.order.service.impl;

import cn.tedu.charging.common.constant.MqttTopicConst;
import cn.tedu.charging.common.enums.DelayName;
import cn.tedu.charging.common.pojo.message.DelayCheckMessage;
import cn.tedu.charging.common.pojo.message.StartCheckMessage;
import cn.tedu.charging.common.pojo.param.OrderAddParam;
import cn.tedu.charging.common.protocol.JsonResult;
import cn.tedu.charging.common.protocol.WebSocketResult;
import cn.tedu.charging.common.utils.CronUtil;
import cn.tedu.charging.common.utils.SnowflakeIdGenerator;
import cn.tedu.charging.common.utils.XxlJobTaskUtil;
import cn.tedu.charging.order.AMQP.AmqpDelayProducer;
import cn.tedu.charging.order.cilent.DeviceClient;
import cn.tedu.charging.order.cilent.UserClient;
import cn.tedu.charging.order.dao.repository.BillRepository;
import cn.tedu.charging.order.mqtt.producer.MqttProducer;
import cn.tedu.charging.order.points.WebSocketServerPoint;
import cn.tedu.charging.order.pojo.po.ChargingBillSuccessPO;
import cn.tedu.charging.order.service.OrderService;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
public class OrderServiceImplement implements OrderService {

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private DeviceClient deviceClient;

    @Autowired
    private UserClient userClient;

    @Autowired
    private SnowflakeIdGenerator snowflakeIdGenerator;

    @Autowired
    private MqttProducer producer;

    @Autowired
    private WebSocketServerPoint socketServerPoint;

    @Autowired
    private AmqpDelayProducer delayProducer;

    @Override
    public String createOrder(OrderAddParam param) {
        //openFeign调用后端进程设备服务
        log.debug("业务层入参：{}",param);
        //TODO 检查改枪是否可用 【✅  完成】
        JsonResult<Boolean> gunStatusResult = deviceClient.checkGun(param.getGunId());
        if (gunStatusResult.getData()) {
            //如果枪可用
            log.debug("充电枪可用，充电枪状态:{}",param.getGunId());
        }
        else {
            log.debug("充电枪不可用");
            throw new RuntimeException("订单失败,充电枪不可用");
        }
        //TODO 调用用户业务服务,查询车主是否可用改枪 【✅  完成】
        CheckUserAvailable(param.getUserId(), param.getGunId());
        //TODO 使用雪花算法生成唯一性标识符 【✅  完成】
        String billId = snowflakeIdGenerator.nextId() + "";
        log.debug("生成的订单标识吗：{}",billId);
        //todo 和设备通信，发送开始命令 【✅  完成】
        this.sendMessage(param,billId);
        //TODO 发送延迟消息 防止设备没有响应 【✅  完成】
        //.1组织消息对象
        DelayCheckMessage delayCheckMessage = new DelayCheckMessage();
        BeanUtils.copyProperties(param,delayCheckMessage);
        delayCheckMessage.setOrderNo(billId);
        delayCheckMessage.setVehicleId(10);
        //.2发送信息
        //.2.1序列化信息
        String message = JSON.toJSONString(delayCheckMessage);
        delayProducer.sendDelay(
                DelayName.DELAY_EX_CHANGE.getName(),
                DelayName.DELAY_BINGING_KEY.getName(),
                message,
                60000
        );

        //TODO 修改抢状态【✅  完成】
        //TODO 计算该订单的最长执行充电时间 发布定时任务【✅  完成】
        XxlJobTaskUtil.createJobTask(CronUtil.delayCron(1000*60*2),"order-executor",billId);
        return billId;
    }

    @Override
    public void orderStatusCheck(String billId) {
        //调用仓库层
        ChargingBillSuccessPO successPO =
                billRepository.selectSuccessByBillid(billId);
        if (Objects.nonNull(successPO)) {
            Integer billStatus = successPO.getBillStatus();
            if (Objects.nonNull(billStatus) && billStatus == 1) {
                log.info("订单正在充电中……,到达最长时间，发生异常");
                billRepository.updateSuccessBill(billId,3);
                //设置异常订单
                billRepository.saveExeptionalBill(successPO);
                //TODO 推送信息
                WebSocketResult<String> stringWebSocketResult = new WebSocketResult<>();
                stringWebSocketResult.setState(1);
                stringWebSocketResult.setMessage("设备超过指定时间");
                stringWebSocketResult.setData("充电超过指定时间");
                String message = JSON.toJSONString(stringWebSocketResult);
                try {
                    socketServerPoint.pushMessage(message, successPO.getUserId());
                } catch (Exception e) {
                    log.error("发生异常，异常原因;{}",e.getMessage());
                }
            }
        }else {
            log.error("没有成功订单");
        }
    }

    private void CheckUserAvailable(Integer userId, Integer gunId) {
        JsonResult<Boolean> booleanJsonResult = userClient.checkUserStatus(userId, gunId);
        //获取结果
        Boolean isAvailable = booleanJsonResult.getData();
        if (isAvailable) {
            log.debug("用户授权充电：用户id={},枪id={}",userId,gunId);
        }
        else  {
            log.debug("用户不可以在改枪上充电");
            throw new RuntimeException("用户不可以在改枪上充电");
        }
    }

    private void sendMessage(OrderAddParam param,String billId) {
        //设置信息封装
        StartCheckMessage startCheckMessage = new StartCheckMessage();
        startCheckMessage.setOrderNo(billId);
        startCheckMessage.setGunId(param.getGunId());
        startCheckMessage.setUserId(param.getUserId());
        //转换为json字符串
        String message = JSON.toJSON(startCheckMessage).toString();
        //获取主题
        String topic = MqttTopicConst.START_GUN_CHECK_PREFIX+param.getPileId();
        //发送信息
        producer.doSend(topic,message);
    }
}
