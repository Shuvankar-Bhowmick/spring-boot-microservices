package com.app.ecom.dto.response;

import java.math.BigDecimal;

public record CartResponseDto(
        Long id,
        Long productId,
        String productName,
        Integer quantity,
        BigDecimal price
) {
}
