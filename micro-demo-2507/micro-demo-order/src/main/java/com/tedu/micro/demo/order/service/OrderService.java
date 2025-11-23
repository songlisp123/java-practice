package com.tedu.micro.demo.order.service;

public interface OrderService {
    //接收前端参数，并生成订单编号
    String createOrder(Integer user_id,Integer gun_id);

}
