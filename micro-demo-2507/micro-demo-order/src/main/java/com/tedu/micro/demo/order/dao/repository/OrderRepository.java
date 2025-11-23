package com.tedu.micro.demo.order.dao.repository;

import com.tedu.micro.demo.order.pojo.po.OrderInfoPo;

public interface OrderRepository {

    void saveOrder(OrderInfoPo order);
}
