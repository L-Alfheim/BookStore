package com.twt.bookstore.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 用户注册请求数据传输对象
 * @param username 用户名
 * @param password 密码
 * @param email 邮箱
 * @param phoneNumber 手机
 * 
 */
public record UserRegister(
        @NotBlank(message = "username should not be blank")
        String username,

        @NotBlank(message = "password should not be blank")
        @Size(min = 6, max = 20, message = "the length of the password should be between 6 and 20")
        String password,

        @NotBlank(message = "email sshould not be blank")
        @Email(message = "the format of email is not correct")
        String email,

        @NotBlank(message = "phone number should not be blank")
        String phoneNumber
) {
}