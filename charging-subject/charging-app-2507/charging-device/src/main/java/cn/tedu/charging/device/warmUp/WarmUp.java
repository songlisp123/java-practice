package cn.tedu.charging.device.warmUp;

import cn.tedu.charging.device.service.WormUpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class WarmUp implements ApplicationRunner {

    @Autowired
    private WormUpService wormUpService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.debug("场站数据预加载开始");
        wormUpService.warm();
        log.debug("场站预加载成功");
    }
}
