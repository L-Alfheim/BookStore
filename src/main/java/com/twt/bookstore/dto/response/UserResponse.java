package com.twt.bookstore.dto.response;

import com.twt.bookstore.poju.UserRole;

/**
 * 用户登录成功响应数据传输对象
 * @param token JWT Token
 * @param username 用户名
 * @param role 角色
 */
public record UserResponse(
        String token,
        String username,
        UserRole role
) {
}