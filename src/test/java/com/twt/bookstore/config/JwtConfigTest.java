// JwtConfigBindingTest.java

package com.twt.bookstore.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;

import com.twt.bookstore.config.jwt.JwtConfig;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(properties = {
    "jwt.secret-key=835ffd9adf626587f9f597f7360b69e7473e97878ada4f142445a40f2ba8a562",
    "jwt.expiration-time=120000"
})
// 确保在测试类中只启用一次配置属性
@EnableConfigurationProperties(JwtConfig.class)
public class JwtConfigTest {

    @Autowired
    private JwtConfig properties;

    @Test
    void propertiesShouldBeBoundCorrectly() {
        // 1. 断言密钥被正确加载
        assertEquals("835ffd9adf626587f9f597f7360b69e7473e97878ada4f142445a40f2ba8a562", properties.getSecretKey(), 
                     "Secret key should match the test configuration.");

        // 2. 断言过期时间被正确加载和转换
        assertEquals(120000L, properties.getExpirationTime(), 
                     "Expiration time should match the test configuration.");
    }
}