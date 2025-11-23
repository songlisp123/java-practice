package com.snl.xxl.job.demo.timer;

import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class XxlJobTimer {

    @Value("${xxl.job.executor.port:19999}")
    private String port;

    @XxlJob("demoJobHandler")
    public void execute() {
        log.info("正在执行任务……,来自端口【{}】",port);
    }
}
