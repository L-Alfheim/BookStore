package com.twt.bookstore.poju;

/**
 * 角色枚举
 * 用户，管理员
 */
public enum UserRole {
    admin,user;
    public static UserRole roleFromString(String roleString) {
        return UserRole.valueOf(roleString);
    }
}
