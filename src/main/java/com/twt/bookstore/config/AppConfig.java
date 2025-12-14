package com.twt.bookstore.config;

import org.apache.ibatis.type.TypeHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.twt.bookstore.handler.UuidTypeHandler;
import com.twt.bookstore.util.OrderIdGenerator;

/**
 * 集中注册一些工具组件
 */
@Configuration
public class AppConfig {
    /**
     * 注册 UUID 到 VARCHAR36 的转换器
     */
    @Bean
    public TypeHandler<?> uuidTypeHandler() {
        return new UuidTypeHandler();
    }

    /**
     * 注册雪花算法的订单号生成
     */
    @Bean
    public OrderIdGenerator orderIdGenerator() {
        long datacenterId = 1L;
        long workerId = 1L;
        return new OrderIdGenerator(datacenterId, workerId);
    }
}
