package com.twt.bookstore.poju;

import java.time.Instant;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 购物车类
 */
@Setter
@Getter
@NoArgsConstructor
public class Cart {
    private Long id;
    private Long userId;       // 用户ID
    private Long bookId;       // 商品ID
    private Integer itemCount;  //数量
    private boolean isDeleted; // 是否删除
    private Instant createdAt; // 加入时间
    private Instant deleteAt;  // 删除时间

    /**
     * 用于数据库构造
     * @param id
     * @param userId
     * @param bookId
     * @param itemCount
     * @param createdAt
     */
    public Cart(Long id, Long userId, Long bookId, Integer itemCount, Instant createdAt) {
        this.id = id;
        this.userId = userId;
        this.bookId = bookId;
        this.itemCount = itemCount;
        this.createdAt = createdAt;
    }

    /**
     * 新加商品构造函数
     * @param userId 用户主键id
     * @param bookId 书籍主键id
     * @param itemCount 数量
     * @param createdAt 加入时间
     */
    public Cart(Long userId, Long bookId, Integer itemCount, Instant createdAt) {
        this.userId = userId;
        this.bookId = bookId;
        this.itemCount = itemCount;
        this.createdAt = createdAt;
    }

    public Cart(Long id, Long userId, Long bookId, Integer itemCount, boolean isDeleted, Instant createdAt,
            Instant deleteAt) {
        this.id = id;
        this.userId = userId;
        this.bookId = bookId;
        this.itemCount = itemCount;
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
        this.deleteAt = deleteAt;
    }
    
}
