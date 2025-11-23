package com.tedu.micro.demo.device.service.impl;

import com.tedu.micro.demo.common.vo.GunInfoVo;
import com.tedu.micro.demo.device.dao.repository.DeviceRepository;
import com.tedu.micro.demo.device.pojo.po.GunInfoPo;
import com.tedu.micro.demo.device.service.DeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
public class DeviceServiceImpl implements DeviceService {

    @Autowired
    private DeviceRepository deviceRepository;
    @Override
    public GunInfoVo getGunStatus(Integer gunId) {
        log.debug("入参：{}",gunId);
        GunInfoVo gunInfoVo = new GunInfoVo();
        GunInfoPo gunById = deviceRepository.getGunById(gunId);
        if (Objects.equals(gunById,null)) {
            log.debug("未找到查询结果");
            return null;
        }
        BeanUtils.copyProperties(gunById,gunInfoVo);
        return gunInfoVo;
    }
}
