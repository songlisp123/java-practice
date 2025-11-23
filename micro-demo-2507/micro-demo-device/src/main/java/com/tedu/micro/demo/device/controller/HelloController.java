package com.tedu.micro.demo.device.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class HelloController {

    @Value("${server.port}")
    private String port;

    @GetMapping("/device/hello")
    public String hello() {
        return "你好我是来自【%s】的端口".formatted(port);
    }
}
