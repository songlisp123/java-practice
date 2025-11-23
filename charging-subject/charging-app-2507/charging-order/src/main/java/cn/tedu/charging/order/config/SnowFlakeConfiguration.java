package cn.tedu.charging.order.config;

import cn.tedu.charging.common.utils.SnowflakeIdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class SnowFlakeConfiguration {

    @Bean
    public SnowflakeIdGenerator snowflakeIdGenerator() {
        log.debug("正在生成雪花算法");
        return new SnowflakeIdGenerator(1,1);
    }
}
