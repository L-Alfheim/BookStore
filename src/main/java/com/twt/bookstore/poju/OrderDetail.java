package com.twt.bookstore.poju;

import java.math.BigDecimal;

import lombok.Data;

/**
 * 订单明细类
 */
@Data
public class OrderDetail {
    private Long id;
    private Long orderId;        // 所属订单
    private Long bookId;         // 商品ID
    private Integer quantity;    // 数量
    private BigDecimal price;    // 单价
}
