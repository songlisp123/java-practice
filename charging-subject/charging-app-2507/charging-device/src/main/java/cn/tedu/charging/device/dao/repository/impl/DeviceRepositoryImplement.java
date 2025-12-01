package cn.tedu.charging.device.dao.repository.impl;

import cn.tedu.charging.common.pojo.query.NearStationsQuery;
import cn.tedu.charging.device.dao.mapper.GunMapper;
import cn.tedu.charging.device.dao.mapper.StationMapper;
import cn.tedu.charging.device.dao.repository.DeviceRepository;
import cn.tedu.charging.device.pojo.po.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.RedisCommand;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Slf4j
@Repository
public class DeviceRepositoryImplement implements DeviceRepository {

    @Autowired
    private StationMapper stationMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private GunMapper gunMapper;

    @Override
    public List<StationGeoPO> nearStations(NearStationsQuery query) {
        //先从redis数据库中查询场站
        List<StationGeoPO> geoPOS = null;
        GeoOperations<String,Integer> geoOperations = redisTemplate.opsForGeo();
        //调用的是范围查询
        //获取经纬度
        Double latitude = query.getLatitude();
        Double longitude = query.getLongitude();
        //获取中心店
        Point point = new Point(longitude, latitude);
        //获取查询半径
        Double radius = query.getRadius();
        //获取查询覆盖范围
        Circle circle = new Circle(point, radius);
        //携带查询参数条件
        //2.2 携带查询选项 withcoord withdist
        RedisGeoCommands.GeoRadiusCommandArgs arguments=
                RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs();
        arguments.includeCoordinates().includeDistance();
        GeoResults<RedisGeoCommands.GeoLocation<Integer>> geoResults =
                geoOperations.radius("charging:all:station.geo", circle, arguments);
        //4.解析当前查询到的结果
        //4.1先拿到allResults命中的元素集合List
        List<GeoResult<RedisGeoCommands.GeoLocation<Integer>>> geoDatas = geoResults.getContent();
        //4.2如果geoDatas非空 可以循环遍历 解析转化成pos
        if (!geoDatas.isEmpty()) {
            geoPOS = new ArrayList<>();
            for (GeoResult<RedisGeoCommands.GeoLocation<Integer>> geoData : geoDatas) {
                StationGeoPO stationGeoPO = new StationGeoPO();
                //获取场站经纬度
                double lng = geoData.getContent().getPoint().getX();
                double lat = geoData.getContent().getPoint().getY();
                //获取场站距离
                double distance = geoData.getDistance().getValue();
                //获取场站id
                Integer stationId = geoData.getContent().getName();
                //根据场站id查询场站获取该场站的名字和状态
                ChargingStationPO stationPO = this.getStationById(stationId);
                stationGeoPO.setStationId(stationId);
                stationGeoPO.setStationLat(lat);
                stationGeoPO.setStationLng(lng);
                stationGeoPO.setStationName(stationPO.getStationName());
                stationGeoPO.setDistance(BigDecimal.valueOf(distance));
                stationGeoPO.setStationStatus(stationPO.getStationStatus());
                geoPOS.add(stationGeoPO);
            }
        }
        //按照距离大小排序升序排序
        List<StationGeoPO> list =
                geoPOS.stream().sorted(Comparator.comparing(StationGeoPO::getDistance)).toList();

        return list;
    }

    @Override
    public String getStationName(Integer stationId) {
        return "";
    }

    @Override
    public ChargingStationPO getStationById(Integer stationId) {
        //先从redis数据库中读取操作
        ChargingStationPO po = null;
        String key = "charging:station:" + stationId + ".detail";
        ValueOperations<String,ChargingStationPO> valueOperations = redisTemplate.opsForValue();
        ChargingStationPO chargingStationPO = valueOperations.get(key);
        if (Objects.isNull(chargingStationPO)) {
            //向数据库索要数据
            ChargingStationPO selected = stationMapper.selectById(stationId);
            //数据回填到redis数据库
            log.debug("开始向数据库中写入站号为 %d 的充电站详情".formatted(stationId));
            valueOperations.set(key,selected);
            log.debug("写入 %d 的充电站详情成功".formatted(stationId));
            po = selected;
        }
        else {
//            log.debug("该场站已经加载到redis数据库中，不需要操作了！");
            po = chargingStationPO;
        }

        return po;
    }

    @Override
    public List<ChargingGunInfoPO> getStationGuns(Integer stationId) {
        return List.of();
    }

    @Override
    public Boolean updateGunStatus(Integer gunId, Integer status) {
        return null;
    }

    @Override
    public void saveStation(StationCanalPO stationCanalPO) {

    }

    @Override
    public void updateStation(StationCanalPO before, StationCanalPO after) {

    }

    @Override
    public void deleteStation(StationCanalPO stationCanalPO) {

    }

    @Override
    public Long countGunByIdAndStatus(Integer gunId, Integer status) {
        return 0L;
    }

    @Override
    public List<ChargingStationPO> getAllStation() {
        return stationMapper.selectList(null);
        //努力一下

    }

    @Override
    public void saveGeo(List<ChargingStationPO> pos) {
        //TODO【✅ 完成】
        log.debug("开始向reids数据库写入数据");
        //首先获取redis操作模版
        GeoOperations<String,Integer> geoOperations = redisTemplate.opsForGeo();
        //将pos转化为可以读写入数据库的操作
        List<RedisGeoCommands.GeoLocation<Integer>> geos = new ArrayList<>();

        //遍历pos，获取每一个场站的具体信息
        for (ChargingStationPO po : pos) {
            //获取经度
            Double lng = po.getStationLng().doubleValue();
            //获取维度
            Double lat = po.getStationLat().doubleValue();
            //获取场站id
            Integer stationId = po.getId();
            RedisGeoCommands.GeoLocation<Integer> location = new RedisGeoCommands.GeoLocation<>(
                    stationId,new Point(lng,lat));
            geos.add(location);
        }

        log.debug("开始写入到reids数据库中");
        //写入到redis数据库中
        geoOperations.add("charging:all:station.geo",geos);

    }

    @Override
    public boolean haveData() {
        //判断redis是否有改建
        return redisTemplate.hasKey("charging:all:station.geo");
    }

    @Override
    public Boolean getGunById(Integer gunId) {
        ChargingGunInfoPO chargingGunInfoPO = gunMapper.selectById(gunId);
        if (chargingGunInfoPO.getGunStatus() != 1) return false;
        return true;
    }

    @Override
    public ChargingGunInfoPO getStationByGunId(Integer gunId) {
        log.debug("进入到仓库层");
        return null;
    }
}
