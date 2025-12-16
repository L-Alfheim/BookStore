package com.twt.bookstore.dto.request;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CartAddDTO(
    @NotNull(message = "The uuid should not be null")
    UUID uuid, 

    @Positive(message = "The quantity should be positive")
    Integer quantity) {
}
