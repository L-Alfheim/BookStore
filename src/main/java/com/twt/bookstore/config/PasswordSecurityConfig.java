package com.twt.bookstore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 安全配置
 * 用于实例化加盐hash密码的编码器
 */
@Configuration
public class PasswordSecurityConfig {

    /**
     * 使用 BCryptPasswordEncoder 进行加盐哈希加密
     * @return PasswordEncoder 密码编码器
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
