package cn.tedu.charging.order.service.impl;

import cn.tedu.charging.common.pojo.message.CheckResultMessage;
import cn.tedu.charging.common.pojo.message.DelayCheckMessage;
import cn.tedu.charging.common.protocol.WebSocketResult;
import cn.tedu.charging.order.cilent.DeviceClient;
import cn.tedu.charging.order.dao.repository.BillRepository;
import cn.tedu.charging.order.points.WebSocketServerPoint;
import cn.tedu.charging.order.pojo.po.ChargingBillFailPO;
import cn.tedu.charging.order.pojo.po.ChargingBillSuccessPO;
import cn.tedu.charging.order.service.ConsumerService;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
public class ConsumerServiceImplement implements ConsumerService {

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private DeviceClient deviceClient;

    @Autowired
    private WebSocketServerPoint webSocketServerPoint;

    @Override
    public void handleCheckNoRes(DelayCheckMessage msg) {
        //TODO 延迟信息消费业务 【✅ 完成】
        //获取订单信息
        String orderNo = msg.getOrderNo();

        //查询成功订单
        long countSuccessOrder = billRepository.countSuccessOrder(orderNo);
        if (countSuccessOrder == 0) {
            //不存在成功订单
            long failOrder = billRepository.countFailOrder(orderNo);
            if (failOrder == 0) {
                //不存在失败订单:自检无响应,创建失败订单
                ChargingBillFailPO chargingBillFailPO = new ChargingBillFailPO();
                chargingBillFailPO.setBillId(orderNo);
                chargingBillFailPO.setFailDesc("设备无响应");
                chargingBillFailPO.setGunId(msg.getGunId());
                chargingBillFailPO.setUserId(msg.getUserId());
                chargingBillFailPO.setVehicleId(msg.getVehicleId());
                chargingBillFailPO.setUpdateTime(new Date());
                chargingBillFailPO.setCreateTime(new Date());
                chargingBillFailPO.setDeleted(0);
                billRepository.saveFailOrder(chargingBillFailPO);
                //TODO 更新抢状态 【✅ 完成】
                deviceClient.updateGunStatus(msg.getGunId());
                //TODO 推送信息
                WebSocketResult<String> stringWebSocketResult = new WebSocketResult<>();
                stringWebSocketResult.setState(1);
                stringWebSocketResult.setMessage("设备自检失败");
                String data = "设备无响应,请换枪";
                stringWebSocketResult.setData(data);
                String message = JSON.toJSONString(stringWebSocketResult);
                try {
                    webSocketServerPoint.pushMessage(message, msg.getUserId());
                }catch (Exception e) {
                    log.error("发生异常，异常原因:{}",e.getMessage());
                }
            }
        }

    }

    @Override
    public void handlerCheckResult(CheckResultMessage msg) {
        ///从信息对象解析result
        //TODO 发送消息【✅ 完成】
        Boolean result = msg.getResult();
        WebSocketResult<String> stringWebSocketResult = new WebSocketResult<>();
        stringWebSocketResult.setState(1);
        stringWebSocketResult.setMessage("设备自检反馈的信息");
        if (result) {
            //根据订单id查询当前订单
            long count = billRepository.countSuccessOrder(msg.getOrderNo());
            if (count>0) {
                //存在成功订单
                log.debug("存在成功订单");
                return;
            }
            log.debug("订单处理自检设备枪成功");
            //TODO 储存成功订单信息 【✅ 完成】
            ChargingBillSuccessPO chargingBillSuccessPO = new ChargingBillSuccessPO();
            chargingBillSuccessPO.setBillId(msg.getOrderNo());
            chargingBillSuccessPO.setGunId(msg.getGunId());
            chargingBillSuccessPO.setUserId(msg.getUserId());
            //TODO 根据枪id查询场站id，供应商id
            /*
            TODO
             */
            //TODO 根据用户id查询汽车id
            /*
            todo
             */
            //将订单改编为正在充电中
            chargingBillSuccessPO.setBillStatus(1);
            chargingBillSuccessPO.setCreateTime(new Date());
            //设置充电开始时间
            chargingBillSuccessPO.setChargingStartTime(new Date());
            chargingBillSuccessPO.setUpdateTime(new Date());
            chargingBillSuccessPO.setDeleted(0);
            //持久化数据
            saveSuccessOrder(chargingBillSuccessPO);
            //TODO 组织成功启动后订单的信息对象【✅ 完成】
            String data = "订单创建成功！开始充电";
            stringWebSocketResult.setData(data);
        } else {
            log.debug("设备自检失败");
            //TODO 储存失败订单信息 【✅ 完成】
            long count = billRepository.countFailOrder(msg.getOrderNo());
            if (count>0) {
                //存在成功订单
                log.debug("存在失败订单");
                return;
            }
            //获取订单失败po对象
            ChargingBillFailPO chargingBillFailPO = new ChargingBillFailPO();
            chargingBillFailPO.setBillId(msg.getOrderNo());
            chargingBillFailPO.setFailDesc(msg.getFailDesc());
            chargingBillFailPO.setGunId(msg.getGunId());
            chargingBillFailPO.setUserId(msg.getUserId());
            //TODO 根据枪id查询场站id
            /*
            TODO
             */
            //TODO 根据用户id查询汽车id
            /*
            todo
             */
            chargingBillFailPO.setCreateTime(new Date());
            chargingBillFailPO.setUpdateTime(new Date());
            chargingBillFailPO.setDeleted(0);
            //持久化数据
            saveFailBill(chargingBillFailPO);
            //TODO 储存失败订单信息 【✅ 完成】
            String data = "您的订单创建失败,送你一张优惠卷";
            stringWebSocketResult.setData(data);
        }
        //推送信息
        String message = JSON.toJSONString(stringWebSocketResult);
        try {
            webSocketServerPoint.pushMessage(message,msg.getUserId());
        }catch (Exception exception) {
            log.error("发生异常：{}",exception.getMessage());
        }
    }

    private void saveSuccessOrder(ChargingBillSuccessPO chargingBillSuccessPO) {
        billRepository.saveSuccessOrder(chargingBillSuccessPO);
    }

    private void saveFailBill(ChargingBillFailPO chargingBillFailPO) {
        billRepository.saveFailOrder(chargingBillFailPO);
    }
}
