package com.app.ecom.dto.response;

import java.math.BigDecimal;

public record CartResponseDto(
        Long cartItemId,
        Long productId,
        String productName,
        Integer quantity,
        BigDecimal price
) {
}
