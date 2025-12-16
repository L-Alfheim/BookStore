package com.twt.bookstore.handler;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.twt.bookstore.dto.response.BaseResponse;

/**
 * 权限不足处理类 (CustomAccessDeniedHandler)。
 * 用于处理已认证用户但访问受限资源时抛出的 AccessDeniedException (403 错误)。
 * 此时应返回 403 Forbidden 状态码和自定义 JSON 错误信息。
 * @author Gemini
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * @title handle
     * @description 处理权限不足异常，向客户端返回 403 状态码和 JSON 错误信息。
     *
     * @param request 当前的 HTTP 请求。
     * @param response 当前的 HTTP 响应。
     * @param accessDeniedException 捕获到的权限不足异常。
     * @throws IOException 如果写入响应时发生 I/O 错误。
     * @throws ServletException 如果发生 Servlet 相关的错误。
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {

        // 1. 设置响应状态码为 403 Forbidden
        response.setStatus(HttpStatus.FORBIDDEN.value());
        // 2. 设置响应内容类型为 JSON
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        
        // 3. 构建自定义错误响应体
        // 假设错误码 403 代表权限不足
        BaseResponse<Void> result = BaseResponse.error(403, "Access Denied: You do not have sufficient permissions to access this resource.");
        
        // 4. 将响应对象转换为 JSON 字符串并写入响应体
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
}
