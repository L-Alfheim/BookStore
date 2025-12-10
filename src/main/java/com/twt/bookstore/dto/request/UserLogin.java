package com.twt.bookstore.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * 用户登录请求数据传输对象
 * @param username 用户名
 * @param password 密码
 */
public record UserLogin(
        @NotBlank(message = "username should not be blank")
        String username,

        @NotBlank(message = "password should not be blank")
        String password
) {
}
