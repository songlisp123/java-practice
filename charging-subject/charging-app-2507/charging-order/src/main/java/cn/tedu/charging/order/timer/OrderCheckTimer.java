package cn.tedu.charging.order.timer;

import cn.tedu.charging.order.service.OrderService;
import com.xxl.job.core.context.XxlJobContext;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderCheckTimer {

    @Autowired
    private OrderService orderService;
    /**
     * 检查订单是否结束
     * 结束：1
     * 失败 非1
     * 调度中心在任务中jobHandler名字order-status-order
     */

    @XxlJob("order-status-check")
    public void orderCheck() {
        //1.接收参数
        String billId= XxlJobContext.getXxlJobContext().getJobParam();
        //2.打印简单日志
        log.debug("订单检查任务开始执行,参数:{}",billId);
        //处理订单
        orderService.orderStatusCheck(billId);
    }
}
