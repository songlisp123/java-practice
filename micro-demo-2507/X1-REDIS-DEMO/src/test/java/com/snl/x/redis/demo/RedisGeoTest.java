package com.snl.x.redis.demo;

import com.snl.x1.redis.demo.RedisApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest(classes = RedisApplication.class)
public class RedisGeoTest {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void test() {
        System.out.println("当前模板是："+redisTemplate);
    }
}
