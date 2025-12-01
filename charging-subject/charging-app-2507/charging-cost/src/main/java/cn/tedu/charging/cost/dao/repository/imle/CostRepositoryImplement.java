package cn.tedu.charging.cost.dao.repository.imle;

import cn.tedu.charging.cost.dao.mapper.CostRuleMapper;
import cn.tedu.charging.cost.dao.repository.CostRuleRepository;
import cn.tedu.charging.cost.pojo.po.ChargingCostRulePO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CostRepositoryImplement implements CostRuleRepository {

    @Autowired
    private CostRuleMapper costRuleMapper;

    @Override
    public ChargingCostRulePO getCostRule(Integer stationId, Integer hour) {
        QueryWrapper<ChargingCostRulePO> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("station_id",stationId);
        queryWrapper.le("start_time",hour);
        queryWrapper.gt("end_time",hour);
        return costRuleMapper.selectOne(queryWrapper);
    }
}
