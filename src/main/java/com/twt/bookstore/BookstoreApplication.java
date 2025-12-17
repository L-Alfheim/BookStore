package com.twt.bookstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;

import com.twt.bookstore.config.jwt.JwtConfig;

@SpringBootApplication
@ServletComponentScan
@EnableConfigurationProperties(JwtConfig.class)
// @MapperScan("com.twt.bookstore.mapper")
public class BookstoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookstoreApplication.class, args);
	}

}
