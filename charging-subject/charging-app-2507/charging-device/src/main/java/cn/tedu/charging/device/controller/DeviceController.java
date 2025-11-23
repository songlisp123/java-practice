package cn.tedu.charging.device.controller;

import cn.tedu.charging.common.pojo.query.NearStationsQuery;
import cn.tedu.charging.common.pojo.vo.GunInfoVO;
import cn.tedu.charging.common.pojo.vo.StationDetailVO;
import cn.tedu.charging.common.pojo.vo.StationInfoVO;
import cn.tedu.charging.common.protocol.JsonResult;
import cn.tedu.charging.device.service.DeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 设备相关http入口 业务包含
 * operater
 * station
 * pile
 * gun
 * @author: xiaoxw
 * @date: 2025/8/29
 * @version: 1.0
 */
@RestController
@Slf4j
public class DeviceController {
    @Autowired
    private DeviceService deviceService;

    /**
     * 查询附近的充电站
     * @param query 充电站查询参数 经纬度 半径
     * @return 充电站列表
     */
    @GetMapping("/device/station/near")
    public JsonResult<List<StationInfoVO>> nearStations(NearStationsQuery query){
        //TODO
        log.debug("入参:{}",query);
        List<StationInfoVO> stationInfoVOS = deviceService.nearStations(query);
        return JsonResult.ok(stationInfoVOS);
    }
    //查询某个充电站详情包括站场信息以及站场关联的枪数据
    @GetMapping("/device/station/detail/{stationId}")
    public JsonResult<StationDetailVO> detailStation(@PathVariable Integer stationId){
        //TODO
        log.debug("查询的当前充电站的id是：{}",stationId);
        StationDetailVO stationDetailVO = deviceService.detailStation(stationId);
        return JsonResult.ok(stationDetailVO);
    }
    //订单调用设备检查枪是否可用
    @GetMapping("/device/gun/check")
    public JsonResult<Boolean> checkGun(@RequestParam("gunId") Integer gunId){
        //TODO
        Boolean aBoolean = deviceService.checkGunAvailable(gunId);
        return JsonResult.ok(aBoolean);
    }

    //修改枪状态的方法
    @PostMapping("/device/gun/error")
    public JsonResult<Boolean> updateGunStatus(
            @RequestParam("gunId")Integer gunId){
        //TODO 枪状态故障
        log.error("强状态异常");
        return JsonResult.ok();
    }

    @GetMapping("/device/station/{gunId}")
    public JsonResult<GunInfoVO> selectStationByGunId(@PathVariable("gunId") Integer gunId) {
        log.debug("根据强id查询场站id");
        GunInfoVO vo = deviceService.selectStationByGunId(gunId);
        return JsonResult.ok(vo);
    }
}
