package cn.tedu.charging.order.cilent;

import cn.tedu.charging.common.protocol.JsonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("charging-user")
public interface UserClient {

    @GetMapping("/user/charge/check")
    JsonResult<Boolean> checkUserStatus(@RequestParam("userId") Integer userId,
                                        @RequestParam("gunId") Integer gunId);
}
