package com.twt.bookstore.dto.userContext;

import java.util.UUID;

import com.twt.bookstore.poju.UserRole;

/**
 * 从JWT中解析出的用户身份信息
 */
public record UserContext(String userName,
                          UUID uuid,
                          UserRole role
) {
    /**
     * 从String 构造UserContext
     * @param userName 用户名
     * @param uuidString uuid 字符串
     * @param roleString role字符串
     * @return
     */
    public static UserContext fromString(String userName, String uuidString, String roleString) {
        UUID uuid = UUID.fromString(uuidString);
        UserRole role = UserRole.roleFromString(roleString);
        return new UserContext(userName, uuid, role);
    }
}
