package com.twt.bookstore.security;

import java.util.Date;
import java.util.HexFormat;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import com.twt.bookstore.config.JwtConfig;
import com.twt.bookstore.exception.JwtSecurityException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

/**
 * 核心JWT组件
 * 提供了基础相关的JWT方法
 * JWT相关的内部异常 代码203
 */
@Component
public class Jwt {

    private final SecretKey key;
    private final Long expirationTime;

    public Jwt(JwtConfig properties) {
        try{
            // 加载配置文件
            String hexKey = properties.getSecretKey();
            expirationTime = properties.getExpirationTime();
            System.out.println(hexKey);
            byte[] byteKey = hexToBytes(hexKey);

            key = Keys.hmacShaKeyFor(byteKey);
        } catch(Exception e) {
            System.err.println("JWT config load error, server shut down");
            throw new IllegalStateException("JWT config error");
        }
    }

    /**
     * 十六进制字符转化二进制数组
     * @param hex 十六进制字符
     * @return 二进制数组
     */
    protected static byte[] hexToBytes(String hex) {
        HexFormat hexFormat = HexFormat.of();
        return hexFormat.parseHex(hex);
    }

    /**
     * 生成 JWT Token
     * @param username 用户名
     * @param claims 其他标记
     * @return JWT Token
     * @throws JwtSecurityException
     */
    public String generateToken(String username, Map<String, Object> claims) throws JwtSecurityException{
        try {
            return Jwts.builder()
                    .setClaims(claims) // 自定义声明
                    .setSubject(username) // 主题
                    .setIssuedAt(new Date()) // 签发时间
                    .setExpiration(new Date(System.currentTimeMillis() + expirationTime)) // 过期时间
                    .signWith(key, SignatureAlgorithm.HS256) // 签名算法
                    .compact();
        } catch (Exception e) {
            throw new JwtSecurityException(203, "errors occured when generating JWT token", e);
        }
    }

    /**
     * JWT 验证
     * @param token JWT字符串
     * @return 解包后的JWT信息
     * @throws JwtSecurityException 101 验证不通过，需要返回前端
     *                              203 JWT解析发生内部错误
     */
    public Claims validateAndParseToken(String token) throws JwtSecurityException {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            // JWT 过期需要重新登录
            throw new JwtSecurityException(101, "JWT expired", e); 
        } catch (JwtException e) {
            // 签名错误或格式错误
            throw new JwtSecurityException(101, "Invalid JWT signature or format", e);
        } catch (Exception e) {
            throw new JwtSecurityException(203, "Inner error occured when checking JWT tokens", e);
        }
    }
}
