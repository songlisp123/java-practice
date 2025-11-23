package com.tedu.micro.demo.order.dao.repository.imp;

import com.tedu.micro.demo.order.dao.mapper.OrderMapper;
import com.tedu.micro.demo.order.dao.repository.OrderRepository;
import com.tedu.micro.demo.order.pojo.po.OrderInfoPo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class OrderRepositoryImplement implements OrderRepository {

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public void saveOrder(OrderInfoPo order) {
        log.debug("仓库层入参:{}",order);
        orderMapper.insert(order);
    }
}
