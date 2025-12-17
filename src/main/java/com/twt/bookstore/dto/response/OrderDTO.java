package com.twt.bookstore.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;


//订单简表DTO
public record OrderDTO(
    String OrderIdTime, //订单号
    UUID uuid,  //下单用户
    BigDecimal totalPrice,  //总金额
    Instant createTime //下单时间 
) {

}
