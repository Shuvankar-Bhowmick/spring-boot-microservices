package com.app.ecom.dto.request;

import java.math.BigDecimal;

public record ProductRequestDto(
        String name,
        String description,
        BigDecimal price,
        Integer stockQuantity,
        String category,
        String imageUrl

        /* active by default is going to be true hence we don't pass it through the request
         * but make it active be default inside the entity itself */
) {
}
