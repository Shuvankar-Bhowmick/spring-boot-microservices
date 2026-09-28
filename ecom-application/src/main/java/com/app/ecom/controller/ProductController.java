package com.app.ecom.controller;

import com.app.ecom.dto.request.ProductRequestDto;
import com.app.ecom.dto.response.ProductResponseDto;
import com.app.ecom.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponseDto> createProduct(@RequestBody ProductRequestDto product) {
        // Implement the logic to create a product
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(product));
    }
}
