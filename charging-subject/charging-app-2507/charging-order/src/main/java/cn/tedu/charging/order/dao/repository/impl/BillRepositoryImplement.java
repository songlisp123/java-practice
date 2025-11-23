package cn.tedu.charging.order.dao.repository.impl;

import cn.tedu.charging.order.dao.mapper.BillFailMapper;
import cn.tedu.charging.order.dao.mapper.BillSuccessMapper;
import cn.tedu.charging.order.dao.repository.BillRepository;
import cn.tedu.charging.order.pojo.po.ChargingBillFailPO;
import cn.tedu.charging.order.pojo.po.ChargingBillSuccessPO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BillRepositoryImplement implements BillRepository {

    @Autowired
    private BillFailMapper failMapper;

    @Autowired
    private BillSuccessMapper billSuccessMapper;

    @Override
    public void saveFailOrder(ChargingBillFailPO chargingBillFailPO) {
        failMapper.insert(chargingBillFailPO);
    }

    @Override
    public void saveSuccessOrder(ChargingBillSuccessPO chargingBillSuccessPO) {
        billSuccessMapper.insert(chargingBillSuccessPO);
    }

    @Override
    public long countSuccessOrder(String orderNo) {
        QueryWrapper<ChargingBillSuccessPO> wrapper = new QueryWrapper<>();
        wrapper.eq("bill_id",orderNo);
        List<ChargingBillSuccessPO> chargingBillSuccessPOS = billSuccessMapper.selectList(wrapper);
        return chargingBillSuccessPOS.size();
    }

    @Override
    public long countFailOrder(String orderNo) {
        QueryWrapper<ChargingBillFailPO> wrapper = new QueryWrapper<>();
        wrapper.eq("bill_id",orderNo);
        List<ChargingBillFailPO> chargingBillSuccessPOS = failMapper.selectList(wrapper);
        return chargingBillSuccessPOS.size();
    }
}
