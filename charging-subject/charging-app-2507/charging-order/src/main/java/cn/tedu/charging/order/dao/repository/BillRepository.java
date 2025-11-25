package cn.tedu.charging.order.dao.repository;

import cn.tedu.charging.order.pojo.po.ChargingBillFailPO;
import cn.tedu.charging.order.pojo.po.ChargingBillSuccessPO;

public interface BillRepository {
    void saveFailOrder(ChargingBillFailPO chargingBillFailPO);

    void saveSuccessOrder(ChargingBillSuccessPO chargingBillSuccessPO);

    long countSuccessOrder(String orderNo);

    long countFailOrder(String orderNo);

    ChargingBillSuccessPO selectSuccessByBillid(String billId);

    void updateSuccessBill(String billId,Integer status);

    void saveExeptionalBill(ChargingBillSuccessPO successPO);
}
