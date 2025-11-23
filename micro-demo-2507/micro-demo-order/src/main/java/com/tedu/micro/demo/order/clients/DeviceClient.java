package com.tedu.micro.demo.order.clients;

import com.tedu.micro.demo.common.protocol.JsonResult;
import com.tedu.micro.demo.common.vo.GunInfoVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Device就是我们当前订单想要调用的目标服务提供者的
 */
//当前接口在框架实现的时候 具体调用的目标 url地址
@FeignClient(name="device-service"/*,url="http://localhost:8000"*/)
public interface DeviceClient {
    //抽象方法的定义 明确告知底层框架要具体访问的接口
    /**
     * GET method
     * Path /device/gun/info/{gunId}
     * 请求参数 @PathVariable Integer gunId
     * 返回值 JsonResult<GunInfoVO>
     * 完全使用springmvc的注解来定义接口
     */
    @GetMapping("/device/gun/info/{gunId}")
    JsonResult<GunInfoVo> checkGunStatus(@PathVariable Integer gunId);
}
