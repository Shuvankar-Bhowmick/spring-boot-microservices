package com.app.ecom.service;

import com.app.ecom.dto.request.CartItemRequestDto;
import com.app.ecom.model.CartItem;
import com.app.ecom.repository.CartItemRepository;
import com.app.ecom.repository.ProductRepository;
import com.app.ecom.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

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
}
