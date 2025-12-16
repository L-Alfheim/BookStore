package com.twt.bookstore.handler;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.twt.bookstore.dto.response.BaseResponse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 未登录处理类 (CustomAccessDeniedHandler)
 * 用于处理未登录用户访问受限api时抛出的 401
 * 此时应返回 401 Unauthorized 状态码和自定义 JSON 错误信息。
 */
@Component
public class CustomAuthenticationHandler implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * @title commence
     * @description 处理未登录时访问受限api，向客户端返回 401 状态码和 JSON 错误信息。
     *
     * @param request 当前的 HTTP 请求。
     * @param response 当前的 HTTP 响应。
     * @param accessDeniedException 捕获到的权限不足异常。
     * @throws IOException 如果写入响应时发生 I/O 错误。
     * @throws ServletException 如果发生 Servlet 相关的错误。
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        // 1. 设置响应状态码为 401 Unauthorized
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        // 2. 设置响应内容类型为 JSON
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        
        // 3. 构建自定义错误响应体
        // 假设错误码 403 代表权限不足
        BaseResponse<Void> result = BaseResponse.error(401, "Access Denied: You need to login first.");
        
        // 4. 将响应对象转换为 JSON 字符串并写入响应体
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
}

