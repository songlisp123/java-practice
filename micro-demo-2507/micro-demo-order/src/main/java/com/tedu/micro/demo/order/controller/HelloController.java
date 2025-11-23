package com.tedu.micro.demo.order.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class HelloController {

    @Value("${server.port}")
    private String port;

    @GetMapping("/order/hello")
    private String hello() {
        return "你好来自端口[%s]的友好提示".formatted(port);
    }
}
