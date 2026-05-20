package com.twt.bookstore.security;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.twt.bookstore.dto.userContext.UserContext;
import com.twt.bookstore.exception.JwtSecurityException;
import com.twt.bookstore.poju.UserRole;
import com.twt.bookstore.security.filter.JwtAuthenticationFilter;
import com.twt.bookstore.security.util.Jwt;

/**
 * @title JwtAuthenticationFilterTest
 * @description JwtAuthenticationFilter 的单元测试类。
 * 使用 Mockito 模拟依赖项，并使用 MockMvc 模拟 HTTP 请求和过滤器链。
 */
@ExtendWith(MockitoExtension.class)
public class JwtAuthenticationFilterTest {

    // 注入被测试对象
    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    // 模拟 Jwt 工具类
    @Mock
    private Jwt mockJwtUtil;

    // MockMvc 用于模拟 Spring MVC 环境和过滤器链
    private MockMvc mockMvc;

    // 定义用于测试的常量
    private static final String MOCK_JWT_TOKEN = "valid.jwt.token";
    private static final String HEADER_STRING = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";

    /**
     * @title setup
     * @description 在每个测试方法执行前初始化 MockMvc 和 SecurityContext。
     */
    @BeforeEach
    public void setup() {
        // 使用 ReflectionTestUtils 注入 @Value 配置的默认值
        ReflectionTestUtils.setField(jwtAuthenticationFilter, "headerString", HEADER_STRING);
        ReflectionTestUtils.setField(jwtAuthenticationFilter, "tokenPrefix", TOKEN_PREFIX);
        
        // 如果您在 filter 中使用了 ObjectMapper，则也需要设置它
        // 注意：由于 ObjectMapper 不是通过 @RequiredArgsConstructor 注入的，这里需要手动设置（如果需要测试异常处理逻辑）
        // 简单测试可以忽略，但为了完整性，最好设置。
        ReflectionTestUtils.setField(jwtAuthenticationFilter, "objectMapper", new ObjectMapper());


        // 搭建 MockMvc，将 JwtAuthenticationFilter 添加到过滤器链中
        // 这里的 NoOpController 只是一个占位符，确保 MockMvc 可以执行到过滤器链的末端。
        mockMvc = MockMvcBuilders
                .standaloneSetup(new NoOpController())
                .addFilters(jwtAuthenticationFilter)
                .build();
        
        // 清除 SecurityContext，确保每个测试都是干净的
        SecurityContextHolder.clearContext();
    }

    /**
     * @title testValidTokenAuthenticationSuccess
     * @description 测试携带有效 JWT Token 的请求，验证认证是否成功设置。
     * @throws Exception MockMvc 抛出的异常。
     */
    @Test
    public void testValidTokenAuthenticationSuccess() throws Exception {
        // 模拟 UserContext 对象，用于模拟 Token 解析结果
        UserContext mockUserContext = UserContext.fromString(
                "testUser",
                UUID.randomUUID().toString(), 
                UserRole.admin.toString()
        );
        
        // 模拟 JwtUtil 的行为：当传入有效的 Token 时，返回 UserContext
        when(mockJwtUtil.validateAndParseToken(MOCK_JWT_TOKEN)).thenReturn(mockUserContext);

        // **测试变量放置 Token 的方式：**
        // 使用 mockMvc 模拟 GET 请求，并在请求头中设置 Authorization
        mockMvc.perform(get("/test/resource")
                // 将 TOKEN_PREFIX 和 MOCK_JWT_TOKEN 组合并放置在 Authorization Header 中
                .header(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + MOCK_JWT_TOKEN))
                .andExpect(status().isOk()); // 预期请求能够成功到达 Controller

        // 验证 SecurityContextHolder 中是否设置了认证信息
        // Principal 应该就是 UserContext 中的 userName
        assert SecurityContextHolder.getContext().getAuthentication() != null;
        assert SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("testUser");
        assert SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
               .anyMatch(a -> a.getAuthority().equals(UserRole.admin.toString()));
    }

    /**
     * @title testMissingTokenNoAuthentication
     * @description 测试请求缺少 Token 时，验证认证上下文是否未设置，并且请求放行。
     * @throws Exception MockMvc 抛出的异常。
     */
    @Test
    public void testMissingTokenNoAuthentication() throws Exception {
        // 模拟不带 Authorization Header 的请求
        mockMvc.perform(get("/test/resource"))
                .andExpect(status().isOk()); // 预期请求放行到 Controller
        
        // 验证 SecurityContextHolder 中没有认证信息
        assert SecurityContextHolder.getContext().getAuthentication() == null;
        
        // 验证 JwtUtil 的验证方法没有被调用
        verify(mockJwtUtil, never()).validateAndParseToken(anyString());
    }
    
    /**
     * @title testInvalidTokenReturnsUnauthorized
     * @description 测试携带无效 JWT Token 的请求，验证是否返回 401 错误响应。
     * @throws Exception MockMvc 抛出的异常。
     */
    @Test
    public void testInvalidTokenReturnsUnauthorized() throws Exception {
        // 模拟 JwtUtil 的行为：当传入无效的 Token 时，抛出 JwtSecurityException
        JwtSecurityException securityException = new JwtSecurityException(101, "Token Expired", null);
        when(mockJwtUtil.validateAndParseToken(MOCK_JWT_TOKEN)).thenThrow(securityException);

        // 模拟带 Token 的请求
        mockMvc.perform(get("/test/resource")
                .header(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + MOCK_JWT_TOKEN))
                // 预期请求被 Filter 拦截并返回 401 Unauthorized
                .andExpect(status().isUnauthorized()); 
        
        // 验证 SecurityContextHolder 中没有认证信息
        assert SecurityContextHolder.getContext().getAuthentication() == null;
    }

    // 仅用于 MockMvc 搭建的占位 Controller
    @org.springframework.web.bind.annotation.RestController
    class NoOpController {
        @org.springframework.web.bind.annotation.GetMapping("/test/resource")
        public String testResource() {
            return "ok";
        }
    }
}
