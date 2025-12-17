package com.twt.bookstore.mapper;

import java.util.UUID;

import com.twt.bookstore.poju.UserInfo;

public interface UserRepository {

    /**
     * 按照uuid 查找主键id
     * @param uuid
     * @return id Long
     */
    Long queryIdByUUID(UUID uuid);

    /**
     * 按照主键id 查找uuid
     * @param 主键id
     * @return uuid UUID
     */
    public UUID queryUuidById(Long userId);

    /**
     * 按照UUID查找用户
     * @param uuid
     * @return UserInfo
     */
    UserInfo queryByUUID(UUID uuid);

        /**
     * 按照用户名查找用户
     * @param name 用户名
     * @return UserInfo
     */
    public UserInfo queryByUsername(String name);

    /**
     * 添加人员信息
     * @param userInfo UserInfo 人员信息对象
     * @return int 受影响的行数
     */
    int insertUserFields(UserInfo userInfo);

    /**
     * 更新人员信息
     * @param userInfo
     * @return int 受影响的行数
     */
    int updateUserFields(UserInfo userInfo);

}