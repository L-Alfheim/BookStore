package com.twt.bookstore.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

//订单明细DTO
public record OrderDetailDTO(
    UUID uuid,  //商品UUID
    String title,   //商品名称
    Integer quantity,   //商品数量
    BigDecimal singlePriceBigDecimal    //商品单价
) {

}
