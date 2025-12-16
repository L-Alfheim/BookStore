package com.twt.bookstore.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * 书籍的泛查询
 * 支持分页
 */
public record BookQueryDTO(
                            String title,
                            String author,

                            @PositiveOrZero(message = "page should be Positive or Zero")
                            Integer page,

                            //最大20
                            @PositiveOrZero(message = "limit should be Positive or Zero")
                            @Max(value = 20, message = "The max of size is 20")
                            Integer size
){}
