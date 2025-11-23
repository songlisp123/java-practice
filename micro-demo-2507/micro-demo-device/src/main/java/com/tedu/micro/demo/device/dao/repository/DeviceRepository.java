package com.tedu.micro.demo.device.dao.repository;

import com.tedu.micro.demo.device.pojo.po.GunInfoPo;

public interface DeviceRepository {

    GunInfoPo getGunById(Integer id);
}
