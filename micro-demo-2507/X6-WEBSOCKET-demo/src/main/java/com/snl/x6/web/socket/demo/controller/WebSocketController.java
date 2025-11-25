package com.snl.x6.web.socket.demo.controller;

import com.snl.x6.web.socket.demo.points.WebSocketServerPoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class WebSocketController {

    @Autowired
    private WebSocketServerPoint socketServerPoint;

    @GetMapping("/push")
    public String push(String message,Integer userId) {
        socketServerPoint.pushMessage(message,userId);
        return "【✅】 发送成功";
    }
}
