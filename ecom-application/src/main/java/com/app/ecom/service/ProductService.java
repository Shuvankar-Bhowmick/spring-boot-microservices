package com.app.ecom.service;

import com.app.ecom.dto.request.ProductRequestDto;
import com.app.ecom.dto.response.ProductResponseDto;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    ProductResponseDto createProduct(ProductRequestDto product);

    Optional<ProductResponseDto> updateProduct(Long id, ProductRequestDto product);

    List<ProductResponseDto> getProducts();

    boolean deleteProduct(Long id);

    List<ProductResponseDto> searchProducts(String keyword);
}
