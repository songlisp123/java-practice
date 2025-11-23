package com.tedu.micro.demo.order.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tedu.micro.demo.order.pojo.po.OrderInfoPo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<OrderInfoPo> {
}
