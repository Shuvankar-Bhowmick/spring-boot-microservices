package com.app.ecom.dto.request;

import lombok.Data;

@Data
public class CartItemRequestDto {
    private Long productId;
    private Integer quantity;
}
