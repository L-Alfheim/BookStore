package com.twt.bookstore.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public record CartDTO(UUID uuid,
                      String title,
                      String author,
                      Integer quantity,
                      BigDecimal price,
                      Boolean isAvailable
) {

}
