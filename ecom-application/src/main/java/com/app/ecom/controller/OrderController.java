package com.app.ecom.controller;

import com.app.ecom.dto.response.OrderResponseDto;
import com.app.ecom.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(@RequestHeader("X-User-ID") String userId) {
        // Implement the logic to place an order
        OrderResponseDto orderResponse = orderService.placeOrder(userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponse);
    }
}
