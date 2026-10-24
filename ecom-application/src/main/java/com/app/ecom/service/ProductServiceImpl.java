package com.app.ecom.service;

import com.app.ecom.dto.request.ProductRequestDto;
import com.app.ecom.dto.response.ProductResponseDto;
import com.app.ecom.model.Product;
import com.app.ecom.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
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

    @Override
    public List<ProductResponseDto> getProducts() {
        return productRepository.findByActiveTrue().stream()
                .map(ProductServiceImpl::mapToProductResponseFromProduct)
                .toList();
    }

    @Override
    public boolean deleteProduct(Long id) {
        // mark the product as inactive instead of deleting it from the database
        return productRepository.findById(id)
                .map(product -> {
                    product.setActive(false);
                    productRepository.save(product);
                    return true;
                })
                .orElse(false);
    }

    @Override
    public List<ProductResponseDto> searchProducts(String keyword) {
        return productRepository.searchProducts(keyword).stream()
                .map(ProductServiceImpl::mapToProductResponseFromProduct)
                .toList();
    }
}