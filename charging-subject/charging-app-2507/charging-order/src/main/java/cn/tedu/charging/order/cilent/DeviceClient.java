package cn.tedu.charging.order.cilent;

import cn.tedu.charging.common.protocol.JsonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("charging-device")
public interface DeviceClient {

    @GetMapping("/device/gun/check")
    JsonResult<Boolean> checkGun(@RequestParam("gunId") Integer gunId);

    @PostMapping("/device/gun/error")
    public JsonResult<Boolean> updateGunStatus(
            @RequestParam("gunId")Integer gunId);
}
