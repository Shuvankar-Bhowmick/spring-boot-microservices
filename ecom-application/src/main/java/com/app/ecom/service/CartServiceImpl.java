package com.app.ecom.service;

import com.app.ecom.dto.request.CartItemRequestDto;
import com.app.ecom.dto.response.CartResponseDto;
import com.app.ecom.exception.ResourceNotFoundException;
import com.app.ecom.model.CartItem;
import com.app.ecom.repository.CartItemRepository;
import com.app.ecom.repository.ProductRepository;
import com.app.ecom.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Override
    public boolean addToCart(String userId, CartItemRequestDto request) {
        // perform validations first

        // validate the productId and quantity in the request
        var product = productRepository.findById(request.getProductId());
        if (product.isEmpty()) {
            return false;
        }

        if (product.get().getStockQuantity() < request.getQuantity()) {
            return false;
        }

        var user = userRepository.findById(Long.valueOf(userId));
        if (user.isEmpty()) {
            return false;
        }


        // process the user cart now
        var existingCartItem = cartItemRepository.findByUserAndProduct(user.get(), product.get());
        if (existingCartItem != null) {
            // update the quantity of the existing cart item
            existingCartItem.setQuantity(existingCartItem.getQuantity() + request.getQuantity());
            existingCartItem.setPrice(
                    product.get().getPrice().multiply(
                            BigDecimal.valueOf(existingCartItem.getQuantity())));
            cartItemRepository.save(existingCartItem);
        } else {
            // create a new cart item
            var newCartItem = new CartItem();
            newCartItem.setUser(user.get());
            newCartItem.setProduct(product.get());
            newCartItem.setQuantity(request.getQuantity());
            newCartItem.setPrice(product.get().getPrice().multiply(BigDecimal.valueOf(request.getQuantity())));
            cartItemRepository.save(newCartItem);
        }
        return true;
    }

    @Override
    public boolean removeFromCart(String userId, Long productId) {
        var product = productRepository.findById(productId);
        if (product.isEmpty()) {
            return false;
        }

        var user = userRepository.findById(Long.valueOf(userId));
        if (user.isEmpty()) {
            return false;
        }

        var cartItem = cartItemRepository.findByUserAndProduct(user.get(), product.get());
        if (cartItem == null) {
            return false;
        }

        cartItemRepository.delete(cartItem);
        return true;
    }

    @Override
    public List<CartResponseDto> getCartForUser(String userId) {
        var user = userRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        return cartItemRepository.findByUser(user).stream()
                .map(this::toCartResponseDto)
                .toList();
    }

    private CartResponseDto toCartResponseDto(CartItem cartItem) {
        return new CartResponseDto(
                cartItem.getId(),
                cartItem.getProduct().getId(),
                cartItem.getProduct().getName(),
                cartItem.getQuantity(),
                cartItem.getPrice()
        );
    }
}
