package cn.tedu.charging.order.service;

import cn.tedu.charging.common.pojo.message.CheckResultMessage;
import cn.tedu.charging.common.pojo.message.DelayCheckMessage;

public interface ConsumerService {

    void handleCheckNoRes(DelayCheckMessage msg);

    void handlerCheckResult(CheckResultMessage msg);
}
