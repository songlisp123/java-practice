package cn.tedu.charging.cost.service.impl;

import cn.tedu.charging.common.pojo.param.ProgressCostParam;
import cn.tedu.charging.common.pojo.vo.ProgressCostVO;
import cn.tedu.charging.cost.dao.repository.CostRuleRepository;
import cn.tedu.charging.cost.pojo.po.ChargingCostRulePO;
import cn.tedu.charging.cost.service.CostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Slf4j
@Service
public class CosyServiceImplement  implements CostService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private CostRuleRepository repository;
    @Override
    public ProgressCostVO calculateCost(ProgressCostParam param) {
        log.debug("接受参数：{}",param);
        //TODO 获取系统时间【✅ 完成】
        Integer hour = getCurrentHour();
        //TODO 根据当前条件【✅ 完成】
        ChargingCostRulePO costRule =
                getCostRule(param.getStationId(), 1, hour);
        //TODO 计算单次度数【✅ 完成】
        BigDecimal onceCapacity =calculateOnceCapacity(param);
        //TODO 利用单价和单次度数计算金额【✅ 完成】
        BigDecimal totalCost=calculateTotalCost(onceCapacity,costRule,param);
        //TODO 封装vo返回【✅ 完成】
        ProgressCostVO progressCostVO = new ProgressCostVO();
        progressCostVO.setTotalCost(totalCost.doubleValue());
        progressCostVO.setPowerFee(costRule.getPowerFee().doubleValue());
        progressCostVO.setChargingCapacity(onceCapacity.doubleValue());
        return progressCostVO;
    }

    private BigDecimal calculateTotalCost(BigDecimal onceCapacity, ChargingCostRulePO costRule, ProgressCostParam param) {
        //1.利用单次度数 和 单价 计算 本次金额
        BigDecimal currentCost = costRule.getPowerFee().multiply(onceCapacity);
        //2.利用redis 对绑定orderNo的key值做总金额的累加increment
        ValueOperations<String,Double> valueOps = redisTemplate.opsForValue();
        String totalCostKey="charging:order:total:"+param.getOrderNo();
        //3.调用increment
        Double totalCost = valueOps.increment(totalCostKey, currentCost.doubleValue());
        log.debug("订单的总金额:{}",totalCost);
        return new BigDecimal(totalCost);
    }

    private BigDecimal calculateOnceCapacity(ProgressCostParam param) {
        //获取操作redis 读写上次总度数的客户端string操作对象
        ValueOperations<String,Double> valueOps = redisTemplate.opsForValue();
        //1.准备一个key值,存储上次总度数,每张订单每次充电进度 都是同一个订单
        String orderLastCapacityKey="charging:order:laster:total:"+param.getOrderNo();
        //2.执行读取上次的总度数,将本次总度数写进key 在下一次用来当做上次总度数使用
        //set key currentTotalCapaicity GET
        Double lastTotal = valueOps.getAndSet(orderLastCapacityKey, param.getTotalCapacity());
        if (lastTotal==null){
            log.debug("第一次获取上次总度数,key:{}",orderLastCapacityKey);
            lastTotal=0.0;
        }
        //3.相减得到单次充电度数
        /*Double capacity=param.getTotalCapacity()-lastTotal;double直接减法 可能出现精度偏差*/
        return new BigDecimal(param.getTotalCapacity()).subtract(new BigDecimal(lastTotal));
    }

    private ChargingCostRulePO getCostRule(Integer stationId, int i, Integer hour) {
        return repository.getCostRule(stationId, hour);
    }

    private Integer getCurrentHour() {
        return LocalDateTime.now().getHour();
    }
}
