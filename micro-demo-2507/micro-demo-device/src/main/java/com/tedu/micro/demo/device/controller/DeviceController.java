package com.tedu.micro.demo.device.controller;

import com.tedu.micro.demo.common.protocol.JsonResult;
import com.tedu.micro.demo.common.vo.GunInfoVo;
import com.tedu.micro.demo.device.service.DeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    @GetMapping("/device/gun/info/{gunId}")
    public JsonResult<GunInfoVo> checkGunStatus(@PathVariable Integer gunId)
    {
        log.debug("控制器层传参:{}",gunId);
        GunInfoVo gunStatus = deviceService.getGunStatus(gunId);
        return JsonResult.success(gunStatus);
    }

}
