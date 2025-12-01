package com.twt.bookstore.mapper;

import java.util.UUID;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import com.twt.bookstore.exception.SqlException;
import com.twt.bookstore.poju.UserInfo;

public class UserRepositoryImpl implements UserRepository {

    //获取sqlSessionTemplate
    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    /**
     * 按照uuid 查找单一用户
     * 如果要获取用户主键id，也使用这个方法
     * @param uuid
     * @return result UserInfo 若不存在返回null
     * @throws SqlException 202
     */
    @Override
    public UserInfo queryByUUID(UUID uuid) throws SqlException {
        try {
            return sqlSessionTemplate.selectOne("queryByUUID", uuid);
        } catch(Exception e) {
            //查询出现错误
            throw new SqlException(202, "errors occurs when queryByUUID", e);
        }
    }
    
    /**
     * 添加人员信息
     * @param userInfo UserInfo 人员信息对象
     * @return int 受影响的行数
     * @throws SqlException 101 需要判断是否是由于手机，邮箱重复造成异常
     */
    @Override
    public int insertUserFields(UserInfo userInfo) throws SqlException {
        
        try {
            return sqlSessionTemplate.insert("insertUserFields", userInfo);
        } catch (Exception e) {
            throw new SqlException(101, "errors occurs when insertUserFields", e);
        }
    }

    /**
     * 更新人员信息
     * @param userInfo
     * @return int 受影响的行数
     * @throws SqlException 101 需要判断是否是由于手机，邮箱重复造成异常
     */
    @Override
    public int updateUserFields(UserInfo userInfo) throws SqlException {
        try {
            return sqlSessionTemplate.update("updateUserFields", userInfo);
        } catch (Exception e) {
            throw new SqlException(101, "errors occurs when updateUserFields", e);
        }
    }
}
