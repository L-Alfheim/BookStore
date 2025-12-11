package com.twt.bookstore.config;

import org.apache.ibatis.type.TypeHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 1. 禁用 CSRF 保护：对于无状态的 API (例如您的 POST 请求) 是必要的
            .csrf(csrf -> csrf.disable()) 
            
            // 2. 配置授权规则
            .authorizeHttpRequests(auth -> auth
                //允许所有人登录
                .requestMatchers("/api/auth/register", "/api/auth/login").permitAll()

                // 允许所有人访问 /books 路径下的所有请求 (GET, POST, PUT, DELETE)
                .requestMatchers("/books/**").permitAll() 
                
                // 任何其他请求（如 /admin）仍然需要认证
                .anyRequest().authenticated() 
            ); 
            
        return http.build();
    }

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
