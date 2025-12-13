package com.twt.bookstore.config;

import org.apache.ibatis.type.TypeHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.twt.bookstore.handler.UuidTypeHandler;

@Configuration
public class MybatisConfig {
        /**
     * 将自定义的 UuidTypeHandler 注册到 MyBatis 配置中。
     * * 注意：在 Spring Boot 默认配置下，通常只需将 TypeHandler 标记为 @Component 或位于 Mybatis 扫描路径下即可。
     * 此处使用 @Bean 明确声明，以确保其被正确识别和加载。
     */
    @Bean
    public TypeHandler<?> uuidTypeHandler() {
        return new UuidTypeHandler();
    }

}
