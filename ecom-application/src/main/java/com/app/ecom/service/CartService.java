package com.app.ecom.service;

import com.app.ecom.dto.request.CartItemRequestDto;
import com.app.ecom.dto.response.CartResponseDto;

import java.util.List;

public interface CartService {
    boolean addToCart(String userId, CartItemRequestDto request);

    boolean removeFromCart(String userId, Long productId);

    List<CartResponseDto> getCartForUser(String userId);
}
