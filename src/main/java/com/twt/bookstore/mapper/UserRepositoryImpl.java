package com.twt.bookstore.mapper;

import java.util.UUID;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.twt.bookstore.exception.SqlException;
import com.twt.bookstore.poju.UserInfo;

@Repository
public class UserRepositoryImpl implements UserRepository {

    //获取sqlSessionTemplate
    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    /**
     * 按照uuid 查找主键id
     * @param uuid
     * @return result UserInfo 若不存在返回null
     */
    @Override
    public Long queryIdByUUID(UUID uuid) {
        return sqlSessionTemplate.selectOne("queryIdByUUID", uuid);
    }

    /**
     * 按照UUID查找用户
     * @param uuid
     * @return UserInfo
     * @throws SqlException 202 数据库错误
     */
    @Override
    public UserInfo queryByUUID(UUID uuid) {
        return sqlSessionTemplate.selectOne("queryByUUID", uuid);
    }
    
    /**
     * 按照用户名查找用户
     * @param name 用户名
     * @return UserInfo
     */
    @Override
    public UserInfo queryByUsername(String name) {
        return sqlSessionTemplate.selectOne("queryByUsername", name);
    }
    
    /**
     * 添加人员信息
     * @param userInfo UserInfo 人员信息对象
     * @return int 受影响的行数
     */
    @Override
    public int insertUserFields(UserInfo userInfo) {
        return sqlSessionTemplate.insert("insertUserFields", userInfo);
    }

    /**
     * 更新人员信息
     * @param userInfo
     * @return int 受影响的行数
     */
    @Override
    public int updateUserFields(UserInfo userInfo) {
        return sqlSessionTemplate.update("updateUserFields", userInfo);
    }
}
