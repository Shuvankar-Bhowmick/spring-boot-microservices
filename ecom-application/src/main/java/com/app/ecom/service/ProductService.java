package com.app.ecom.service;

import com.app.ecom.dto.request.ProductRequestDto;
import com.app.ecom.dto.response.ProductResponseDto;

public interface ProductService {
    ProductResponseDto createProduct(ProductRequestDto product);
}
