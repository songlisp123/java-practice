package cn.tedu.charging.order.service.impl;

import cn.tedu.charging.common.pojo.message.CheckResultMessage;
import cn.tedu.charging.common.pojo.message.DelayCheckMessage;
import cn.tedu.charging.common.pojo.message.ProgressData;
import cn.tedu.charging.common.pojo.message.ProgressMessage;
import cn.tedu.charging.common.pojo.param.ProgressCostParam;
import cn.tedu.charging.common.pojo.vo.ProgressCostVO;
import cn.tedu.charging.common.protocol.JsonResult;
import cn.tedu.charging.common.protocol.WebSocketResult;
import cn.tedu.charging.common.utils.SnowflakeIdGenerator;
import cn.tedu.charging.common.utils.TimeConverterUtil;
import cn.tedu.charging.order.cilent.CostClient;
import cn.tedu.charging.order.cilent.DeviceClient;
import cn.tedu.charging.order.dao.repository.BillRepository;
import cn.tedu.charging.order.dao.repository.ProcessEsRepository;
import cn.tedu.charging.order.points.WebSocketServerPoint;
import cn.tedu.charging.order.pojo.po.ChargingBillFailPO;
import cn.tedu.charging.order.pojo.po.ChargingBillSuccessPO;
import cn.tedu.charging.order.pojo.po.ChargingProgressEsPO;
import cn.tedu.charging.order.service.ConsumerService;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

@Slf4j
@Service
public class ConsumerServiceImplement implements ConsumerService {

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private DeviceClient deviceClient;

    @Autowired
    private WebSocketServerPoint webSocketServerPoint;

    @Autowired
    private CostClient costClient;

    @Autowired
    private SnowflakeIdGenerator snowflakeIdGenerator;

    @Autowired
    private ProcessEsRepository processEsRepository;

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
                //TODO 推送信息【✅ 完成】
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
             */
            //TODO 根据用户id查询汽车id
            /*
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
             */
            //TODO 根据用户id查询汽车id
            /*
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

    @Override
    public void handleChargingProgress(ProgressMessage message) {
        //获取温度
        chechTemperature(message.getTemperature(),message.getUserId());
        //调用机加中心计算价格
        ProgressCostVO cost = cost(message);
        //保存es持久化对象
        saveChargingProgress(message,cost);
        //TODO 发送消息 【✅ 完成】
        sendProgress2User(cost,message);
        //TODO 更新枪状态 【✅ 完成】
        if (message.getIsFull()) {
            log.debug("订单成功");
            updateSuccessBill(message);
        }else {
            log.debug("计价中心接口：{}",cost);
            log.debug("设备充电中……");
        }

    }

    private void updateSuccessBill(ProgressMessage message) {
        ChargingBillSuccessPO chargingBillSuccessPO = new ChargingBillSuccessPO();
        chargingBillSuccessPO.setBillStatus(2);
        chargingBillSuccessPO.setUpdateTime(new Date());
        chargingBillSuccessPO.setBillId(message.getOrderNo());
        chargingBillSuccessPO.setChargingDuration(message.getTotalTime().intValue());
        chargingBillSuccessPO.setChargingCapacity(message.getTotalCapacity().intValue());
        chargingBillSuccessPO.setChargingEndTime(chargingBillSuccessPO.getUpdateTime());
        billRepository.updateSuccess(chargingBillSuccessPO);
    }

    private void sendProgress2User(ProgressCostVO cost, ProgressMessage message) {
        //TODO 发送计价信息【✅ 完成】
        //封装websocket信息对象
        WebSocketResult<ProgressData> webSocketResult = new WebSocketResult<>();
        //封装属性
        webSocketResult.setState(3);
        webSocketResult.setMessage("充电详情");
        //封装充电数据
        ProgressData progressData = new ProgressData();
        progressData.setTotalCost(cost.getTotalCost());
        progressData.setChargingCapacity(cost.getChargingCapacity());
        progressData.setOneElectricityCost(cost.getPowerFee());
        progressData.setTotalCapacity(message.getTotalCapacity());
        //获取时间
        progressData.setHours(TimeConverterUtil.getHour(message.getTotalTime()).intValue());
        progressData.setMinutes(TimeConverterUtil.getMinute(message.getTotalTime()).intValue());
        progressData.setSeconds(TimeConverterUtil.getSecond(message.getTotalTime()).intValue());

        webSocketResult.setData(progressData);
        //推送信息
        String pushMessage = JSON.toJSONString(webSocketResult);
        try{
            webSocketServerPoint.pushMessage(pushMessage,message.getUserId());
        }catch (Exception e) {
            log.error("发生异常，异常原因：{}",e.getMessage());
        }
    }

    private void saveChargingProgress(ProgressMessage message, ProgressCostVO cost) {
        //TODO 保存充电数据 【✅ 完成】
        //1.组织一个ProgressEsPO
        ChargingProgressEsPO progressEsPO=new ChargingProgressEsPO();
        //1.1消息属性拷贝给po
        BeanUtils.copyProperties(message,progressEsPO);
        //1.2补充 capacity totalCost costVO
        progressEsPO.setChargingCapacity(cost.getChargingCapacity());
        progressEsPO.setTotalCost(cost.getTotalCost());
        //1.3补充一个雪花算法id 目的: 让充电进度有序
        progressEsPO.setId(snowflakeIdGenerator.nextId()+"");
        //2.写入es
        processEsRepository.save(progressEsPO);
    }

    private ProgressCostVO cost(ProgressMessage message) {
        //TODO 调用计价服务 【✅ 完成】
        ProgressCostParam progressCostParam = new ProgressCostParam();
        BeanUtils.copyProperties(message,progressCostParam);
        JsonResult<ProgressCostVO> jsonResult = costClient.calculateCost(progressCostParam);
        if (Objects.nonNull(jsonResult) && jsonResult.getCode() == 0) {
            return jsonResult.getData();
        }else {
            log.error("调用失败");
            throw new RuntimeException("调用异常");
        }
    }

    private void chechTemperature(Double temperature, Integer userId) {
        //TODO 判断物理设备【✅ 完成】
        if (temperature > 100000) {
            log.error("设备温度过高，异常");
            //TODO 通知用户温度异常 【✅ 完成】
            WebSocketResult<String> webSocketResult = new WebSocketResult<>();
            webSocketResult.setState(1);
            webSocketResult.setMessage("充电设备异常");
            webSocketResult.setData("设备温度过高");
            //推送信息
            String pushMessage = JSON.toJSONString(webSocketResult);
            try{
                webSocketServerPoint.pushMessage(pushMessage,userId);
            }catch (Exception e) {
                log.error("发生异常，异常原因：{}",e.getMessage());
            }
        }else {
            log.debug("温度安全");
        }
    }

    private void saveSuccessOrder(ChargingBillSuccessPO chargingBillSuccessPO) {
        billRepository.saveSuccessOrder(chargingBillSuccessPO);
    }

    private void saveFailBill(ChargingBillFailPO chargingBillFailPO) {
        billRepository.saveFailOrder(chargingBillFailPO);
    }
}
