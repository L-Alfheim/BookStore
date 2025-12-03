package com.twt.bookstore.security;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HexFormat;
import java.util.Map;
import java.util.Properties;

import javax.crypto.SecretKey;

import org.apache.ibatis.io.Resources;
import org.springframework.stereotype.Component;

import com.twt.bookstore.exception.JwtSecurityException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

/**
 * 核心JWT组件
 * 提供了基础相关的JWT方法
 */
@Component
public class Jwt {

    private SecretKey key;
    private Long expirationTime;

    public Jwt() {
        try{
            // 加载配置文件
            String resource = "application.properties";
            InputStream inputStream = Resources.getResourceAsStream(resource);

            Properties properties = new Properties();
            properties.load(inputStream);
            String hexKey = properties.getProperty("SECRET_KEY");
            expirationTime = Long.parseLong(properties.getProperty("EXPIRATION_TIME"));
            System.out.println(hexKey);
            byte[] byteKey = hexToBytes(hexKey);

            key = Keys.hmacShaKeyFor(byteKey);
            inputStream.close();
        } catch(IOException e) {
            System.err.println("JWT config load error, server shut down");
            // System.exit(1);
        }
    }

    /**
     * 十六进制字符转化二进制数组
     * @param hex 十六进制字符
     * @return 二进制数组
     */
    public static byte[] hexToBytes(String hex) {
        HexFormat hexFormat = HexFormat.of();
        return hexFormat.parseHex(hex);
    }

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
     * 验证JWT令牌的签名
     * 不会检查过期时间
     *
     * @param token String JWT
     * @return 如果令牌有效且签名正确，返回true。
     * @throws JwtSecurityException 101 JWT验证失效,但不是过期引发的，前端需要重新登录，203内部错误
     */
    public boolean validateToken(String token) throws JwtSecurityException {
        try {
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            //验证失败，重新以101抛出
            throw new JwtSecurityException(101, "validate failed, need relogin", e);
        } catch (Exception e) {
            throw new JwtSecurityException(203, "errors occured when validating JWT", e);
        }
    }

    /**
     * JWT有效期验证
     * @param token String JWT
     * @return flase JWT过期；true 未过期。
     * @throws JwtSecurityException 如果解析令牌过程中发生非过期错误（如签名错误、格式错误等）。
     */
    public boolean isTokenExpired(String token) throws JwtSecurityException {
        try {
            // 尝试解析令牌，如果过期，会抛出 ExpiredJwtException
            Date expiration = parseToken(token).getBody().getExpiration();
            return !expiration.before(new Date());
        } catch (ExpiredJwtException e) {
            // 明确捕获过期异常，表明令牌已过期
            return true;
        } catch (JwtSecurityException e) {
            // 重新抛出由 parseToken 抛出的非过期异常
            throw e;
        }
    }

    /**
     * 解析JWT令牌
     * @param token String JWT
     * @return Jws<Claims> 包含令牌头部和声明(Claims)的Jws对象。
     * @throws JwtSecurityException 203 内部错误
     */
    public Jws<Claims> parseToken(String token) throws JwtSecurityException {
        try {
            return Jwts.parserBuilder()
                       .setSigningKey(key)
                       .build()
                       .parseClaimsJws(token);
        } catch (JwtException e) {
            throw new JwtSecurityException(203, "Errors occurred when parsing JWT token: " + token, e);
        } catch (Exception e) {
            throw new JwtSecurityException(203, "Unknown error during JWT token parsing", e);
        }
    }
}
