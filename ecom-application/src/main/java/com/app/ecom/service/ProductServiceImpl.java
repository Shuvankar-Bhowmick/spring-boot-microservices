package com.app.ecom.service;

import com.app.ecom.dto.request.ProductRequestDto;
import com.app.ecom.dto.response.ProductResponseDto;
import com.app.ecom.model.Product;
import com.app.ecom.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    private static Product updateProductFromRequest(Product product, ProductRequestDto productRequest) {
        product.setName(productRequest.name());
        product.setDescription(productRequest.description());
        product.setPrice(productRequest.price());
        product.setStockQuantity(productRequest.stockQuantity());
        product.setCategory(productRequest.category());
        product.setImageUrl(productRequest.imageUrl());
        return product;
    }

    private static ProductResponseDto mapToProductResponseFromProduct(Product product) {
        return new ProductResponseDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getCategory(),
                product.getImageUrl(),
                product.getActive()
        );
    }

    @Override
    public ProductResponseDto createProduct(ProductRequestDto product) {
        var savedProduct = productRepository.save(updateProductFromRequest(new Product(), product));
        return mapToProductResponseFromProduct(savedProduct);
    }

    @Override
    public Optional<ProductResponseDto> updateProduct(Long id, ProductRequestDto product) {

        // find the product from db
        return productRepository.findById(id)
                .map(existingProduct -> updateProductFromRequest(existingProduct, product))
                .map(productRepository::save)
                .map(ProductServiceImpl::mapToProductResponseFromProduct);
    }
}