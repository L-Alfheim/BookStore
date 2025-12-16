package com.twt.bookstore.poju;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 书籍类
 * 支持全参构造和无参构造
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor 
public class BookInfo {
    private Long id;    //主键id
    private UUID uuid;  //uuid
    private String title;    //书名
    private String author;  //作者
    private BigDecimal price;   //单价
    private String description; //简介
    private Integer stockQuantity;  //库存
    private boolean isAvailable; //在售状态
    private boolean isDeleted;   //删除状态
    private Instant createTime;   //上架时间
    private Instant updateTime; //更新时间
    private Instant deleteTime;   //删除时间

    /**
     * 从uuid构造书本
     * @param uuid UUID 书籍uuid
     */
    public BookInfo(UUID uuid) {
        this.uuid = uuid;
    }

    /**
     * 这个构造器来自数据库字段更新后报错
     * 是缺省updateTime的构造器
     * @param id
     * @param uuid
     * @param title
     * @param author
     * @param price
     * @param description
     * @param stockQuantity
     * @param isAvailable
     * @param isDeleted
     * @param createTime
     * @param deleteTime
     */
    public BookInfo(Long id, UUID uuid, String title, String author, BigDecimal price, String description,
            Integer stockQuantity, boolean isAvailable, boolean isDeleted, Instant createTime, Instant deleteTime) {
        this.id = id;
        this.uuid = uuid;
        this.title = title;
        this.author = author;
        this.price = price;
        this.description = description;
        this.stockQuantity = stockQuantity;
        this.isAvailable = isAvailable;
        this.isDeleted = isDeleted;
        this.createTime = createTime;
        this.deleteTime = deleteTime;
    }
}
