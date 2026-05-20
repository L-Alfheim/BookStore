package com.twt.bookstore.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.twt.bookstore.handler.CustomAccessDeniedHandler;
import com.twt.bookstore.handler.CustomAuthenticationHandler;
import com.twt.bookstore.security.filter.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

/**
 * 访问权限控制器
 * @author Gemini
 */
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    //403处理器
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    //401处理器
    private final CustomAuthenticationHandler customAuthenticationHandler;

    //JWT前置过滤器
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 1. 禁用 CSRF 保护：对于无状态的 API 是必要的
            .csrf(csrf -> csrf.disable()) 
            // 2. 配置授权规则
            .authorizeHttpRequests(auth -> auth
                // 允许所有人访问认证接口（注册/登录）
                .requestMatchers("/api/auth/register", "/api/auth/login").permitAll()

                // 允许所有人访问 /api/books/** 路径下的所有请求 (公开查询/列表)
                .requestMatchers("/api/books/**").permitAll() 
                
                //书籍管理要求admin角色
                .requestMatchers("/api/admin/books/**").hasRole("admin")

                //购物车要求user角色
                .requestMatchers("/api/cart/**").hasRole("user")

                //用户订单要求user角色
                .requestMatchers("/api/orders/**").hasRole("user")

                //管理员订单要求admin角色
                .requestMatchers("/api/admin/orders/**").hasRole("admin")

                // 任何其他请求（如 /admin/users）仍然需要认证
                .anyRequest().authenticated() 
                // .anyRequest().permitAll()

                
            )

            .exceptionHandling(ex -> ex
                .authenticationEntryPoint(customAuthenticationHandler) // 401
                .accessDeniedHandler(customAccessDeniedHandler)            // 403
            );

        http.addFilterBefore(jwtAuthenticationFilter,UsernamePasswordAuthenticationFilter.class);
            
        return http.build();
    }
}