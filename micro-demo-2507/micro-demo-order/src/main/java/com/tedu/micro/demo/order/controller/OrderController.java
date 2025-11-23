package com.tedu.micro.demo.order.controller;

import com.tedu.micro.demo.common.protocol.JsonResult;
import com.tedu.micro.demo.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/order/create")
    public JsonResult<String> create(Integer userId,Integer gunId) {
        log.debug("控制器入参：用户id={},抢id={}",userId,gunId);
        String uuid = orderService.createOrder(userId, gunId);
        return JsonResult.success(uuid);
    }
}
