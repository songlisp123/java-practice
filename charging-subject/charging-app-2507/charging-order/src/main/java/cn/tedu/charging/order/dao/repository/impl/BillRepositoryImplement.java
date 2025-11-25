package cn.tedu.charging.order.dao.repository.impl;

import cn.tedu.charging.order.dao.mapper.BillExceptionMapper;
import cn.tedu.charging.order.dao.mapper.BillFailMapper;
import cn.tedu.charging.order.dao.mapper.BillSuccessMapper;
import cn.tedu.charging.order.dao.repository.BillRepository;
import cn.tedu.charging.order.pojo.po.ChargingBillExceptionPO;
import cn.tedu.charging.order.pojo.po.ChargingBillFailPO;
import cn.tedu.charging.order.pojo.po.ChargingBillSuccessPO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class BillRepositoryImplement implements BillRepository {

    @Autowired
    private BillFailMapper failMapper;

    @Autowired
    private BillSuccessMapper billSuccessMapper;

    @Autowired
    private BillExceptionMapper exceptionMapper;

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

    @Override
    public ChargingBillSuccessPO selectSuccessByBillid(String billId) {
        QueryWrapper<ChargingBillSuccessPO> wrapper = new QueryWrapper<>();
        wrapper.eq("bill_id",billId);
        return billSuccessMapper.selectOne(wrapper);
    }

    @Override
    public void updateSuccessBill(String billId,Integer status) {
        //更新订单状态为异常
        ChargingBillSuccessPO chargingBillSuccessPO = new ChargingBillSuccessPO();
        chargingBillSuccessPO.setBillStatus(status);
        QueryWrapper<ChargingBillSuccessPO> wrapper = new QueryWrapper<>();
        wrapper.eq("bill_id",billId);
        billSuccessMapper.update(chargingBillSuccessPO,wrapper);
    }

    @Override
    public void saveExeptionalBill(ChargingBillSuccessPO successPO) {
        ChargingBillExceptionPO chargingBillExceptionPO = new ChargingBillExceptionPO();
        chargingBillExceptionPO.setBillId(successPO.getBillId());
        chargingBillExceptionPO.setCreateTime(new Date());
        chargingBillExceptionPO.setBillStarttime(successPO.getChargingStartTime());
        chargingBillExceptionPO.setDeleted(0);
        exceptionMapper.insert(chargingBillExceptionPO);
    }
}
