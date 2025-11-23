package com.tedu.micro.demo.device.service;

import com.tedu.micro.demo.common.vo.GunInfoVo;

public interface DeviceService {
    GunInfoVo getGunStatus(Integer gunId);
}
