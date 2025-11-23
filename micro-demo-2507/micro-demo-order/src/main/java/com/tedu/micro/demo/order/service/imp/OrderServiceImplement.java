package com.tedu.micro.demo.order.service.imp;

import com.tedu.micro.demo.common.protocol.JsonResult;
import com.tedu.micro.demo.common.vo.GunInfoVo;
import com.tedu.micro.demo.order.clients.DeviceClient;
import com.tedu.micro.demo.order.dao.repository.OrderRepository;
import com.tedu.micro.demo.order.pojo.po.OrderInfoPo;
import com.tedu.micro.demo.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
public class OrderServiceImplement implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private DeviceClient deviceClient;

    @Override
    public String createOrder(Integer userId, Integer gunId) {
        log.debug("业务层入参：用户id={},墙编号：{}",userId,gunId);
//        todo 这是一个将要实现的逻辑
        Integer status = checkGunStatus(gunId);
        if (status!=1) {
            log.debug("发生异常");
            throw new RuntimeException("暂不使用改枪");
        }
        OrderInfoPo orderInfoPo = new OrderInfoPo();
        orderInfoPo.setUserId(userId);
        orderInfoPo.setGunId(gunId);
        orderInfoPo.setCreateTime(LocalDateTime.now());
        orderInfoPo.setUpdateTime(LocalDateTime.now());
        String billId = UUID.randomUUID().toString().replace("-","");
        orderInfoPo.setBillId(billId);
        orderRepository.saveOrder(orderInfoPo);
        return billId;
    }

    private Integer checkGunStatus(Integer gunId) {
        //调用openFeign
        //1.调用feingClient接口
        JsonResult<GunInfoVo> result = deviceClient.checkGunStatus(gunId);
        //2.读取判断调用结果 如果code!=0说明调用失败 抛出异常逻辑额流程结束
        if (result.getCode()!=0){
            log.error("调用设备服务失败,异常信息:{}",result.getMessage());
            throw new RuntimeException("调用设备服务失败");
        }else{
            GunInfoVo vo = result.getData();
            return vo.getStatus();
        }

    }
}
