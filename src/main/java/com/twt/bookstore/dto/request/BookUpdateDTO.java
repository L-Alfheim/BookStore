package com.twt.bookstore.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * 书籍信息的更新和删除
 */
public record BookUpdateDTO(
                            @NotBlank(message = "title absence")
                            String title,

                            @NotBlank(message = "author absence")
                            String author,

                            @NotNull(message = "price absence")
                            @PositiveOrZero(message = "price should be Positive or Zero")
                            BigDecimal price,
                            
                            String description,
                            
                            @NotNull(message = "stockQuantity absence")
                            @Min(value = 0, message = "stockQuantity should be positive or zero")
                            Integer stockQuantity,

                            boolean isAvailable,
                            boolean isDeleted
) {

}
