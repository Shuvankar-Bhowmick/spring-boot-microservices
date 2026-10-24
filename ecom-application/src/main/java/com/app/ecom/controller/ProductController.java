package com.app.ecom.controller;

import com.app.ecom.dto.request.ProductRequestDto;
import com.app.ecom.dto.response.ProductResponseDto;
import com.app.ecom.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getProducts() {
        // Implement the logic to get all products
        return ResponseEntity.ok().body(productService.getProducts());
    }

    @PostMapping
    public ResponseEntity<ProductResponseDto> createProduct(@RequestBody ProductRequestDto product) {
        // Implement the logic to create a product
        return ResponseEntity.status(HttpStatus.CREATED).body(productService
                .createProduct(product));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDto> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductRequestDto product) {
        // Implement the logic to update a product
        ResponseEntity.status(HttpStatus.OK).body(productService.updateProduct(id, product));
        return productService.updateProduct(id, product)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        return productService.deleteProduct(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductResponseDto>> searchProducts(@RequestParam String keyword) {
        return ResponseEntity.ok(productService.searchProducts(keyword));
    }
}
