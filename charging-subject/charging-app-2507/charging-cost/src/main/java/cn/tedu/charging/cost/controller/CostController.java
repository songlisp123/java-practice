package cn.tedu.charging.cost.controller;

import cn.tedu.charging.common.pojo.param.ProgressCostParam;
import cn.tedu.charging.common.pojo.vo.ProgressCostVO;
import cn.tedu.charging.common.protocol.JsonResult;
import cn.tedu.charging.cost.service.CostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CostController {

    @Autowired
    private CostService costService;

    @PostMapping("/cost/bill/calculate")
    public JsonResult<ProgressCostVO> calculateCost(@RequestBody ProgressCostParam cost){
        //TODO 计算价格【✅ 完成】
        ProgressCostVO progressCostVO = costService.calculateCost(cost);
        return JsonResult.ok(progressCostVO);
    }
}
