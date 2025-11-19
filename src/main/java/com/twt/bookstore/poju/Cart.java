package com.twt.bookstore.poju;

import java.time.Instant;

import lombok.Data;

/**
 * 购物车类
 */
@Data
public class Cart {
    private Long id;
    private Long userId;       // 用户ID
    private Long bookId;       // 商品ID
    private boolean isDeleted; // 是否删除
    private Instant createdAt; // 加入时间
    private Instant deleteAt;  // 删除时间
}
