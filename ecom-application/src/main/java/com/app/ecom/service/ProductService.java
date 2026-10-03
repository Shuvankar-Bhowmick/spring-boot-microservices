package com.app.ecom.service;

import com.app.ecom.dto.request.ProductRequestDto;
import com.app.ecom.dto.response.ProductResponseDto;

import java.util.Optional;

public interface ProductService {
    ProductResponseDto createProduct(ProductRequestDto product);

    Optional<ProductResponseDto> updateProduct(Long id, ProductRequestDto product);
}
