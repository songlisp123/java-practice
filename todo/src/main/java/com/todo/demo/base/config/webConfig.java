package com.todo.demo.base.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class webConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //解决前后端跨域映射的难题
        registry.addMapping("/**") //允许发送的任何请求
                .allowedHeaders("*")    //允许请求头中的任何内容
                .allowedMethods("*")    //允许任何请求方法
                .allowedOriginPatterns("*") //允许跨域操作
                .allowCredentials(true) //请求可以携带会话信息
                .maxAge(3600); //此次请求将会在一小时内注销
    }
}
