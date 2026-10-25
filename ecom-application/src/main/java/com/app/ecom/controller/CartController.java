package com.app.ecom.controller;

import com.app.ecom.dto.request.CartItemRequestDto;
import com.app.ecom.dto.response.CartResponseDto;
import com.app.ecom.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping
    public ResponseEntity<List<CartResponseDto>> getCart(@RequestHeader("X-User-ID") String userId) {
        return ResponseEntity.ok(cartService.getCartForUser(userId));
    }

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

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<String> removeFromCart(
            @RequestHeader("X-User-ID") String userId,
            @PathVariable String productId) {
        if (!cartService.removeFromCart(userId, Long.valueOf(productId))) {
            return ResponseEntity.badRequest().
                    body("User or Product is invalid or Product is not in the cart");
        }
        return ResponseEntity.noContent().build();
    }
}
