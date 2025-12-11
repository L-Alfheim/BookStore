package com.twt.bookstore.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.twt.bookstore.dto.request.UserLogin;
import com.twt.bookstore.dto.response.BaseResponse;
import com.twt.bookstore.dto.response.UserResponse;
import com.twt.bookstore.dto.request.UserRegister;
import com.twt.bookstore.exception.BusinessException;
import com.twt.bookstore.exception.SqlException;
import com.twt.bookstore.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * 认证授权控制器
 * 提供了用户注册和登录接口
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * 用户注册接口
     * POST /api/auth/register
     * 参数：用户名、密码、邮箱
     *
     * @param request 用户注册请求DTO
     * @return BaseResponse + 注册成功信息
     * @throws BusinessException 101
     * @throws SqlException 数据库异常,已在Service里回滚，
     */
    @PostMapping("/register")
    public BaseResponse<UserResponse> register(@Valid @RequestBody UserRegister request)
            throws BusinessException, SqlException {
        
        UserResponse response = authService.register(request);
        return BaseResponse.success(response);
    }

    /**
     * 用户登录接口
     * POST /api/auth/login
     * 参数：用户名、密码
     * 返回：JWT Token、用户名、角色
     *
     * @param request 用户登录请求DTO
     * @return BaseResponse 200 
     * @throws BusinessException 用户名错误、密码错误
     * @throws SqlException 数据库查询异常
     */
    @PostMapping("/login")
    public BaseResponse<UserResponse> login(@Valid @RequestBody UserLogin request)
            throws BusinessException, SqlException {

        UserResponse response = authService.login(request.username(), request.password());
        return BaseResponse.success(response);
    }
}