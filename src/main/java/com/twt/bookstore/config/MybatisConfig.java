package com.twt.bookstore.config;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import java.io.IOException;
import java.io.InputStream;

/**
 * 初始化mybatis配置，初始化失败，程序会中止
 * @author deepseek
 */
@Configuration
@MapperScan("com.twt.bookstore.mapper")
public class MybatisConfig {

    //全局SqlSessionFactory,只创建一次
    private static SqlSessionFactory sqlSessionFactory;
    
    public MybatisConfig() {
        try {
            // 加载mybatis-config.xml配置文件
            String resource = "config/mybatis-config.xml";
            InputStream inputStream = Resources.getResourceAsStream(resource);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
            inputStream.close();
        } catch (IOException e) {
            System.err.println("mybatis-config loads error");
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    /**
     * 创建SqlSessionFactory Bean
     * 单例模式，整个应用共享一个SqlSessionFactory
     */
    @Bean
    public SqlSessionFactory sqlSessionFactory() {
        return sqlSessionFactory;
    }

    /**
     * 创建SqlSession Bean
     * 每次请求创建新的SqlSession，保证线程安全
     * 使用PROTOTYPE作用域确保每个请求都有独立的SqlSession
     */
    @Bean
    @Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
    public SqlSession sqlSession() {
        return sqlSessionFactory.openSession();
    }

    /**
     * 创建SqlSessionTemplate风格的Bean
     * 提供更好的Spring集成体验
     */
    @Bean
    public SqlSessionTemplate sqlSessionTemplate() {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    //获取SqlSessionFactory
    public static SqlSessionFactory getSqlSessionFactory() {
        return sqlSessionFactory;
    }

    //创建新SqlSessionFactory
    public static SqlSession openSession() {
        return sqlSessionFactory.openSession();
    }
}

/**
 * 自定义SqlSessionTemplate，模拟Spring的SqlSessionTemplate行为
 * @author deepseek
 */
class SqlSessionTemplate {
    
    private final SqlSessionFactory sqlSessionFactory;
    
    public SqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }
    
    /**
     * 执行数据库操作，自动管理SqlSession生命周期
     */
    public <T> T execute(SqlSessionCallback<T> callback) {
        SqlSession sqlSession = null;
        try {
            sqlSession = sqlSessionFactory.openSession();
            return callback.doInSqlSession(sqlSession);
        } finally {
            if (sqlSession != null) {
                sqlSession.close();
            }
        }
    }
    
    /**
     * 获取Mapper接口
     */
    public <T> T getMapper(Class<T> type) {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            return sqlSession.getMapper(type);
        }
    }
}

/**
 * SqlSession回调接口
 */
interface SqlSessionCallback<T> {
    T doInSqlSession(SqlSession sqlSession);
}
