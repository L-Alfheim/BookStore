package com.twt.bookstore.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

/**
 * 初始化JWT 的配置类
 */
@ConfigurationProperties(prefix = "jwt")
@Setter
@Getter
public class JwtConfig {
    private String secretKey;
    private Long expirationTime;
}
