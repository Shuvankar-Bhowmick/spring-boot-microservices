package com.app.ecom.service;

import com.app.ecom.dto.request.CartItemRequestDto;

public interface CartService {
    boolean addToCart(String userId, CartItemRequestDto request);
}
