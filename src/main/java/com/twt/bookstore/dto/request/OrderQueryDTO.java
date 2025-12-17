package com.twt.bookstore.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.PositiveOrZero;

public record OrderQueryDTO(
                            @PositiveOrZero(message = "page should be Positive or Zero")
                            Integer page,

                            //最大20
                            @PositiveOrZero(message = "limit should be Positive or Zero")
                            @Max(value = 20, message = "The max of size is 20")
                            Integer size
) {

}
