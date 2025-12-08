package com.twt.bookstore.mapper;

import java.util.UUID;

import com.twt.bookstore.exception.SqlException;
import com.twt.bookstore.poju.UserInfo;

public interface UserRepository {

    /**
     * 按照uuid 查找主键id
     * @param uuid
     * @return result UserInfo 若不存在返回null
     * @throws SqlException 202 数据库错误
     */
    Long queryIdByUUID(UUID uuid) throws SqlException;

    /**
     * 按照UUID查找用户
     * @param uuid
     * @return UserInfo
     * @throws SqlException 202 数据库错误
     */
    UserInfo queryByUUID(UUID uuid) throws SqlException;

    /**
     * 添加人员信息
     * @param userInfo UserInfo 人员信息对象
     * @return int 受影响的行数
     * @throws SqlException 101 需要判断是否是由于手机，邮箱重复造成异常
     *                      202 数据库错误
     */
    int insertUserFields(UserInfo userInfo) throws SqlException;

    /**
     * 更新人员信息
     * @param userInfo
     * @return int 受影响的行数
     * @throws SqlException 101 需要判断是否是由于手机，邮箱重复造成异常
     *                      202 数据库错误
     */
    int updateUserFields(UserInfo userInfo) throws SqlException;

}