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
    private Integer StockQuantity;  //库存
    private boolean isAvalable; //在售状态
    private boolean isDelete;   //删除状态
    private Instant createTime;   //上架时间
    private Instant deleteTime;   //删除时间

    /**
     * 从uuid构造书本
     * @param uuid UUID 书籍uuid
     */
    public BookInfo(UUID uuid) {
        this.uuid = uuid;
    }
}
