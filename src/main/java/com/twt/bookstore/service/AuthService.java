package com.twt.bookstore.service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.twt.bookstore.dto.request.UserLogin;
import com.twt.bookstore.dto.request.UserRegister;
import com.twt.bookstore.dto.response.BaseResponse;
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
     * @param request {@link UserRegister}
     * @throws BusinessException 101 注册信息重复，返回前端
     * @throws SqlException 数据库后端内部错误，回滚
     */
    @Transactional(rollbackFor = BusinessException.class)
    public BaseResponse<UserResponse> register(UserRegister request) throws BusinessException{
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

        try {
            // 插入数据库
            int affectedRows = userRepository.insertUserFields(newUser);

            //数据库数据异常，回滚
            if (affectedRows != 1) {
                throw new BusinessException(202, "the affect row should be 1, but it is not, and no other exception is thrown");
            }

            UserResponse result =  new UserResponse(null, newUser.getUserName(), newUser.getRole());
            return BaseResponse.success(result);

        } catch(DuplicateKeyException e) {
            //手动处理信息重复问题
            return BaseResponse.error(401, "Username or Phone Number or Email has been exsit");
        }
    }

    /**
     * 登录
     *
     * @param request {@link UserLogin}
     * @return BaseResponse<UserResponse> 或者登录错误
     * @throws BusinessException 203
     */
    @Transactional(rollbackFor = BusinessException.class)
    public BaseResponse<UserResponse> login(UserLogin request) throws BusinessException{

        String username = request.username();
        String rawPassword = request.password();
        
        //查询用户信息
        UserInfo user;
        user = userRepository.queryByUsername(username);

        //检查用户存在
        if (user == null) {
            return BaseResponse.error(401, "Username error or user isn't exsit");
        }

        // 校验密码
        if (!passwordEncoder.matches(rawPassword, user.getPasswordEncoder())) {
            return BaseResponse.error(401, "Password error");
        }

        // 生成JWT Token
        try {
            Map<String, Object> claims = new HashMap<>();
            claims.put("username", username);
            claims.put("uuid", user.getUuid().toString());
            claims.put("role", user.getRole().name());

            String token = jwtUtil.generateToken(username, claims);

            // 返回登录信息
            UserResponse result =  new UserResponse(token, user.getUserName(), user.getRole());
            return BaseResponse.success(result);

        } catch (Exception e) {
            throw new BusinessException(203, "JWT Token generate error", e);
        }
    }
}