package com.tedu.micro.demo.order.test;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tedu.micro.demo.order.dao.mapper.OrderMapper;
import com.tedu.micro.demo.order.pojo.po.OrderInfoPo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
public class Ordertest {

    @Autowired
    private OrderMapper orderMapper;

    @Test
    void insert() {
        OrderInfoPo orderInfoPo = new OrderInfoPo();
        orderInfoPo.setBillId("10");
        orderInfoPo.setUserId(10);
        orderInfoPo.setGunId(10);
        orderMapper.insert(orderInfoPo);
    }

    @Test
    void delete() {
        QueryWrapper<OrderInfoPo> objectQueryWrapper =
                new QueryWrapper<>();

        objectQueryWrapper.in("id",1,2);
        orderMapper.delete(objectQueryWrapper);
    }
}
