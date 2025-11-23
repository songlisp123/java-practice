package cn.tedu.charging.order.controller;

import cn.tedu.charging.common.pojo.param.OrderAddParam;
import cn.tedu.charging.common.protocol.JsonResult;
import cn.tedu.charging.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class  OrderController {
    @Autowired
    private OrderService orderService;
    //扫码下单
    @PostMapping("/order/create")
    public JsonResult<String> createOrder(@RequestBody OrderAddParam param){
        //TODO 【❌ 未完成】
        log.debug("订单控制器接收参数：{}",param);
        String billId = orderService.createOrder(param);
        return JsonResult.ok(billId);
    }
}
