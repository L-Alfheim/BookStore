package com.twt.bookstore.security.filter;

import java.io.IOException;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.twt.bookstore.dto.response.BaseResponse;
import com.twt.bookstore.dto.userContext.UserContext;
import com.twt.bookstore.exception.JwtSecurityException;
import com.twt.bookstore.security.util.Jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * JwtAuthenticationFilter
 * JWT 认证过滤器。负责拦截所有请求，从请求头中解析 JWT，验证其有效性，
 * 并将解析出的用户信息注入到 Spring Security 的 SecurityContext 中。
 * 继承自 OncePerRequestFilter 确保每个请求只执行一次过滤。
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    /**
     * JWT 工具类，用于解析和验证 JWT。
     */
    private final Jwt jwtUtil;

    /**
     * JSON 序列化工具。
     */
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * JWT 头部名称，通常是 "Authorization"，从配置中读取。
     */
    @Value("${jwt.header.string:Authorization}")
    private String headerString;
    
    /**
     * JWT 认证方案前缀，通常是 "Bearer "，从配置中读取。
     */
    @Value("${jwt.token.prefix:Bearer }")
    private String tokenPrefix;

    /**
     * 核心过滤逻辑
     * 负责解析请求头中的 JWT，验证并设置 Spring Security 的认证上下文。
     * 如果 Token 验证失败，则直接向客户端写入错误响应。
     * 这是filter chain上的第一个过滤器
     *
     * @param request 当前的 HTTP 请求。
     * @param response 当前的 HTTP 响应。
     * @param filterChain 过滤器链，用于将请求传递给下一个过滤器或目标资源。
     * @throws ServletException 如果发生 Servlet 相关的错误。
     * @throws IOException 如果发生 I/O 错误。
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        
        // 1. 获取 Authorization Header
        String authHeader = request.getHeader(headerString);

        // 2. 检查 Header 是否有效 (Token 存在且以 Bearer 开头)
        if (authHeader == null || !authHeader.startsWith(tokenPrefix)) {
            // 如果没有 Token 或格式错误，直接放行。
            // 允许请求继续，交由后续的 Spring Security 配置（如 AuthenticationEntryPoint）处理未认证请求。
            filterChain.doFilter(request, response);
            return;
        }

        // 3. 提取 JWT 字符串
        String jwtToken = authHeader.substring(tokenPrefix.length());

        try {
            // 4. 解析 JWT 并获取用户身份信息
            UserContext userContext = jwtUtil.validateAndParseToken(jwtToken);

            // 5. 检查用户上下文是否有效，且 SecurityContext 中尚无认证信息
            if (userContext != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                
                // 6. 构建 Authentication 对象 (已验证的 Token，无需凭证)
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userContext.userName(), // Principal: 用户名 (作为身份标识)
                        null,                   // Credentials: 密码/凭证 (Token 已验证，此处为 null)
                        Collections.singletonList(new SimpleGrantedAuthority(userContext.role().name())) // Authorities: 用户的权限/角色列表
                );

                // 7. 设置 Web 认证详情 (记录请求IP、会话ID等信息)
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 8. 将 Authentication 对象存入 SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (JwtSecurityException e) {
            // 9. 处理 Token 验证失败 (如过期、签名错误)
            // 捕获 JwtSecurityException 后，不再抛出异常，而是直接写入 HTTP 错误响应。
            handleJwtSecurityException(response, e);
            return; // 阻止请求继续执行后续的过滤器链
        }

        // 10. 放行请求，继续执行过滤器链
        filterChain.doFilter(request, response);
    }
    
    /**
     * @title handleJwtSecurityException
     * @description 将 JWT 相关的安全异常转换为 HTTP 响应，并写入客户端。
     *
     * @param response 当前的 HTTP 响应。
     * @param exception 捕获到的 JwtSecurityException 异常。
     * @throws IOException 如果写入响应时发生 I/O 错误。
     */
    private void handleJwtSecurityException(HttpServletResponse response, JwtSecurityException exception) throws IOException {
        
        // 设置 HTTP 状态码为 401 Unauthorized
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        
        // 构建统一的错误响应体
        String errorMessage = "JWT verification failed, please login again";
        if (exception.getErrorCode() != 101) {
            errorMessage = "JWT processing error";
        }

        BaseResponse<Void> result = BaseResponse.error(exception.getErrorCode(), errorMessage);

        // 使用 ObjectMapper 将响应对象转换为 JSON 字符串并写入响应体
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
}