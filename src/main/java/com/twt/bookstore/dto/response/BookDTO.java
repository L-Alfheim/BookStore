package com.twt.bookstore.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * 书籍信息的返回
 */
public record BookDTO(  UUID uuid,
                        String title,   
                        String author,
                        BigDecimal price,
                        String description,
                        Integer stockQuantity,
                        boolean isAvailable
    ) {}
