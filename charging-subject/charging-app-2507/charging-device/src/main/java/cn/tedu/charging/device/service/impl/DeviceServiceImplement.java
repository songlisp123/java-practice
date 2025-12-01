package cn.tedu.charging.device.service.impl;

import cn.tedu.charging.common.pojo.query.NearStationsQuery;
import cn.tedu.charging.common.pojo.vo.GunInfoVO;
import cn.tedu.charging.common.pojo.vo.StationDetailVO;
import cn.tedu.charging.common.pojo.vo.StationInfoVO;
import cn.tedu.charging.device.dao.mapper.GunMapper;
import cn.tedu.charging.device.dao.repository.DeviceRepository;
import cn.tedu.charging.device.pojo.po.ChargingGunInfoPO;
import cn.tedu.charging.device.pojo.po.ChargingStationPO;
import cn.tedu.charging.device.pojo.po.StationGeoPO;
import cn.tedu.charging.device.service.DeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class DeviceServiceImplement implements DeviceService {

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private GunMapper gunMapper;

    @Override
    public List<StationInfoVO> nearStations(NearStationsQuery query) {
        List<StationInfoVO> vos = null;
        //查询附近场站
        List<StationGeoPO> stationGeoPOS = deviceRepository.nearStations(query);
        if (Objects.nonNull(stationGeoPOS) && !stationGeoPOS.isEmpty()) {
            //TODO【✅ 完成】
            vos = new ArrayList<>();
            for (StationGeoPO stationGeoPO : stationGeoPOS) {
                //获取场站ID
                Integer stationId = stationGeoPO.getStationId();
                //获取距离
                BigDecimal distance = stationGeoPO.getDistance();
                //获取经纬度
                Double lat = stationGeoPO.getStationLat();
                Double lng = stationGeoPO.getStationLng();
                //根据场站id获取场站vo信息
                ChargingStationPO stationPO = deviceRepository.getStationById(stationId);
                StationInfoVO stationInfoVO = new StationInfoVO();
                stationInfoVO.setStationId(stationId);
                stationInfoVO.setDistance(distance);
                stationInfoVO.setStationLat(lat);
                stationInfoVO.setStationLng(lng);
                stationInfoVO.setStationName(stationPO.getStationName());
                stationInfoVO.setStationStatus(stationPO.getStationStatus());
                vos.add(stationInfoVO);
            }
        }
        return vos;
    }

    @Override
    public StationDetailVO detailStation(Integer stationId) {
        log.debug("业务层参数:{}",stationId);
        StationDetailVO stationDetailVO = new StationDetailVO();
        ChargingStationPO stationPO = deviceRepository.getStationById(stationId);
        BeanUtils.copyProperties(stationPO,stationDetailVO);
        //获取该充电站的所有充电枪
        List<ChargingGunInfoPO> chargingGunInfoPOS = gunMapper.selectByStationId(stationId);
        List<GunInfoVO> gunInfoVOS = chargingGunInfoPOS.stream().map(po -> {
            GunInfoVO gunInfoVO = new GunInfoVO();
            BeanUtils.copyProperties(po, gunInfoVO);
            gunInfoVO.setGunId(po.getId());
            return gunInfoVO;
        }).toList();
        stationDetailVO.setGunInfoVos(gunInfoVOS);
        return stationDetailVO;
    }

    @Override
    public void updateGunStatus(Integer gunId, Integer status) {

    }

    @Override
    public Boolean checkGunAvailable(Integer gunId) {
        log.debug("枪id：{}",gunId);
         return deviceRepository.getGunById(gunId);
    }

    @Override
    public GunInfoVO selectStationByGunId(Integer gunId) {
        log.debug("进入到业务层，枪id={}",gunId);
        ChargingGunInfoPO po = deviceRepository.getStationByGunId(gunId);
        GunInfoVO gunInfoVO = new GunInfoVO();
        BeanUtils.copyProperties(po,gunInfoVO);
        return gunInfoVO;
    }
}
