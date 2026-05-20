package com.twt.bookstore;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.twt.bookstore.security.util.Jwt;


@SpringBootTest
class BookstoreApplicationTests {

	// @Test
	// void contextLoads() {
    //     MybatisConfig mybatis = new MybatisConfig();
    //     System.out.println("连接成功");
	// }

	@Test
	void jwtLoad() {
		Jwt jwt = new Jwt(null);
		System.out.println("load success");
	}

}
