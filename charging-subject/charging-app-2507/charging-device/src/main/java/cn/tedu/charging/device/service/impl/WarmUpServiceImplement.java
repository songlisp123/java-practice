package cn.tedu.charging.device.service.impl;

import cn.tedu.charging.device.dao.repository.DeviceRepository;
import cn.tedu.charging.device.pojo.po.ChargingStationPO;
import cn.tedu.charging.device.pojo.po.StationGeoPO;
import cn.tedu.charging.device.service.WormUpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class WarmUpServiceImplement implements WormUpService {

    @Autowired
    private DeviceRepository deviceRepository;

    @Override
    public void warm() {
        log.debug("场站预加载服务层开启");
        //首先第一步：查询所有场站
        //判断分支，防止每次重启应用都会热加载
        boolean hasData = deviceRepository.haveData();
        if (!hasData) {
            List<ChargingStationPO> geoPos = deviceRepository.getAllStation();
            System.out.printf("共查询场站 %d 座%n", geoPos.size());
            if (Objects.nonNull(geoPos) && !geoPos.isEmpty()) {
                //如果反返回了场站信息
                //那么就保存数据到redis
                deviceRepository.saveGeo(geoPos);
            }
        }
        else {
            log.debug("数据已经预热成功！");
        }
    }
}
