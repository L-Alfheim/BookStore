package com.twt.bookstore.poju;

import java.time.Instant;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 人员基信息
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {
    private Long id;
    private UUID uuid;  //用户识别码
    private String userName;
    private String passwordEncoder;  //密码
    private String phone;   //手机号
    private String email;   //邮箱
    private UserRole role;  //权限角色
    private boolean isDelete;   //删除状态
    private Instant createTime;   //创建时间
    private Instant updateTime;   //更新时间
    private Instant deleteTime;   //删除时间

    /**
     * 从用户名构造人员
     * @param userName String 用户名
     */
    public UserInfo(String userName) {
        this.userName = userName;
    }
}
