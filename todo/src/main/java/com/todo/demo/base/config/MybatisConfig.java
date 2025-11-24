package com.todo.demo.base.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.todo.demo.*.mapper")
public class MybatisConfig {
}
