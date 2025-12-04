package com.twt.bookstore.mapper;

import com.twt.bookstore.exception.SqlException;
import com.twt.bookstore.poju.BookInfo;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@MybatisTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class BookMapperTest {

    @Autowired
    private BookRepository bookMapper;

    @Test
    @Transactional
    @Rollback
    void testFindAllBooksAndDatabaseConnection() {
        try {
            List<BookInfo> books = bookMapper.queryByName("abc");
        } catch (SqlException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
        System.out.println("MyBatis connect success Mapper success");
    }
}
