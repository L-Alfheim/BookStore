package com.twt.bookstore.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 跨域资源共享 (CORS) 全局配置类。
 * <p>
 * 该配置允许前端应用（如 Vue、React）跨域访问后端 API 接口。
 * 默认配置允许来自所有源的常用 HTTP 方法。
 * </p>
 *
 * @author Gemini
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    /**
     * 配置跨域映射规则。
     *
     * @param registry 跨域注册表对象
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 匹配所有接口路径
                .allowedOriginPatterns("*") // 允许所有来源（Spring Boot 2.4+ 推荐使用 pattern）
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 允许的 HTTP 方法
                .allowedHeaders("*") // 允许的所有 Header
                .allowCredentials(true) // 允许携带 Cookie
                .maxAge(3600); // 预检请求（OPTIONS）的缓存时间，单位：秒
    }
}
