package com.app.ecom.controller;

import com.app.ecom.dto.request.CartItemRequestDto;
import com.app.ecom.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @PostMapping
    public ResponseEntity<String> addToCart(
            @RequestHeader("X-User-ID") String userId,
            @RequestBody CartItemRequestDto request) {
        if (!cartService.addToCart(userId, request)) {
            return ResponseEntity.badRequest().
                    body("Product is either out of stock or User not found or Product is invalid");
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
