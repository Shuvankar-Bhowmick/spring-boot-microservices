package com.app.ecom.service;

import com.app.ecom.dto.response.OrderResponseDto;

public interface OrderService {
    OrderResponseDto placeOrder(String userId);
}
