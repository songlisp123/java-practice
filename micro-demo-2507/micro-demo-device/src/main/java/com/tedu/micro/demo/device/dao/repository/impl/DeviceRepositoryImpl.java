package com.tedu.micro.demo.device.dao.repository.impl;

import com.tedu.micro.demo.device.dao.mapper.GunMapper;
import com.tedu.micro.demo.device.dao.repository.DeviceRepository;
import com.tedu.micro.demo.device.pojo.po.GunInfoPo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class DeviceRepositoryImpl implements DeviceRepository {

    @Autowired
    private GunMapper gunMapper;

    @Override
    public GunInfoPo getGunById(Integer id) {
        log.debug("枪id"+id);
        return gunMapper.selectById(id);
    }
}
