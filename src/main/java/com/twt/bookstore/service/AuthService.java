package com.twt.bookstore.service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.twt.bookstore.dto.request.UserRegister;
import com.twt.bookstore.dto.response.UserResponse;
import com.twt.bookstore.exception.BusinessException;
import com.twt.bookstore.exception.SqlException;
import com.twt.bookstore.mapper.UserRepository;
import com.twt.bookstore.poju.UserInfo;
import com.twt.bookstore.poju.UserRole;
import com.twt.bookstore.security.util.Jwt;

import lombok.RequiredArgsConstructor;

/**
 * 认证和授权服务
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;    //数据库接口
    private final PasswordEncoder passwordEncoder; // 用于密码编码器
    private final Jwt jwtUtil;  //JWT 工具

    /**
     * 注册
     *
     * @param request 注册请求
     * @throws BusinessException 101 注册信息重复，返回前端
     * @throws SqlException 数据库后端内部错误，回滚
     */
    @Transactional(rollbackFor = SqlException.class, noRollbackFor = BusinessException.class)
    public UserResponse register(UserRegister request) throws BusinessException, SqlException {
        // 密码是否为空
        if (request.password() == null || request.password().trim().isEmpty()) {
             throw new BusinessException(101, "password should not be blank");
        }

        // 加密密码
        String encodedPassword = passwordEncoder.encode(request.password());

        UserInfo newUser = new UserInfo();
        newUser.setUuid(UUID.randomUUID());
        newUser.setUserName(request.username());
        newUser.setPasswordEncoder(encodedPassword);
        newUser.setPhone(request.phoneNumber());
        newUser.setEmail(request.email());
        newUser.setRole(UserRole.user);
        newUser.setDeleted(false);
        newUser.setCreateTime(Instant.now());
        newUser.setUpdateTime(Instant.now());

        // 插入数据库
        try {
            int affectedRows = userRepository.insertUserFields(newUser);

            //数据库数据异常，回滚
            if (affectedRows != 1) {
                throw new SqlException(202, "the affect row should be 1, but it is not, and no other exception is thrown");
            }

            return new UserResponse(null, newUser.getUserName(), newUser.getRole());
        } catch (SqlException e) {
            // 手动处理101的信息重复，其他自动回滚
            if (e.getErrorCode() == 101) {
                throw new BusinessException(101, "username, phone number or email have exsited", e);
            } else {
                // 其他数据库错误(202)自动回滚
                throw e;
            }
        }
    }

    /**
     * 登录
     *
     * @param username 用户名
     * @param rawPassword 用户明文密码
     * @return UserResponse 
     * @throws BusinessException 101 用户名不存在或密码错误，返回前端
     * @throws SqlException 后端数据库内部错误，回滚
     */
    @Transactional(rollbackFor = SqlException.class, noRollbackFor = BusinessException.class)
    public UserResponse login(String username, String rawPassword) throws BusinessException, SqlException {
        
        //查询用户信息
        UserInfo user;
        try {
            user = userRepository.queryByUsername(username);
        } catch (SqlException e) {
            // 数据库查询错误，回滚
            throw e;
        }
        
        //检查用户存在
        if (user == null) {
            throw new BusinessException(101, "User is not exsit");
        }

        // 校验密码
        if (!passwordEncoder.matches(rawPassword, user.getPasswordEncoder())) {
            throw new BusinessException(101, "password error");
        }

        // 生成JWT Token
        try {
            Map<String, Object> claims = new HashMap<>();
            claims.put("username", username);
            claims.put("uuid", user.getUuid().toString());
            claims.put("role", user.getRole().name());

            String token = jwtUtil.generateToken(username, claims);

            // 返回登录信息
            return new UserResponse(token, user.getUserName(), user.getRole());

        } catch (Exception e) {
            throw new BusinessException(203, "JWT Token generate error", e);
        }
    }
}