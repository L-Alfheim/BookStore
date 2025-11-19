package com.twt.bookstore.poju;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import lombok.Data;

/**
 * 订单大类
 */
@Data
public class Order {
    private Long id;
    private String orderIdTime; // 订单号
    private Long userId;    //所属用户
    private BigDecimal totalPrice;  //总金额
    private Instant createTime;   //创建时间

    // 一对多
    private List<OrderDetail> orderDetails;
}
