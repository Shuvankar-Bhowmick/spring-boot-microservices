package com.app.ecom.service;

import com.app.ecom.dto.OrderItemDto;
import com.app.ecom.dto.response.OrderResponseDto;
import com.app.ecom.exception.ResourceNotFoundException;
import com.app.ecom.model.CartItem;
import com.app.ecom.model.Order;
import com.app.ecom.model.OrderItem;
import com.app.ecom.model.Product;
import com.app.ecom.repository.CartItemRepository;
import com.app.ecom.repository.OrderRepository;
import com.app.ecom.repository.ProductRepository;
import com.app.ecom.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public OrderResponseDto placeOrder(String userId) {
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("User ID must not be blank");
        }

        final long parsedUserId;
        try {
            parsedUserId = Long.parseLong(userId);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("User ID must be a valid number", ex);
        }

        var user = userRepository.findById(parsedUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
        List<CartItem> cartItems = cartItemRepository.findByUser(user);
        if (cartItems.isEmpty()) {
            throw new IllegalStateException("Cannot place an order with an empty cart");
        }

        Order order = new Order();
        order.setUser(user);
        order.setTotalAmount(BigDecimal.ZERO);

        List<CartItem> sortedCartItems = cartItems.stream()
                .sorted(Comparator.comparing(item -> item.getProduct().getId()))
                .toList();

        for (CartItem cartItem : sortedCartItems) {
            Integer quantity = cartItem.getQuantity();
            if (quantity == null || quantity <= 0) {
                throw new IllegalStateException("Cart item quantity must be greater than zero");
            }

            Product product = productRepository.findByIdForUpdate(cartItem.getProduct().getId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Product not found with ID: " + cartItem.getProduct().getId()));
            if (!Boolean.TRUE.equals(product.getActive())) {
                throw new IllegalStateException("Product is no longer available: " + product.getId());
            }
            if (product.getStockQuantity() == null || product.getStockQuantity() < quantity) {
                throw new IllegalStateException("Insufficient stock for product: " + product.getId());
            }

            BigDecimal unitPrice = product.getPrice();
            if (unitPrice == null) {
                throw new IllegalStateException("Product has no price: " + product.getId());
            }

            product.setStockQuantity(product.getStockQuantity() - quantity);
            productRepository.save(product); // Redundant: the managed entity is updated at transaction commit.

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(quantity);
            orderItem.setPrice(unitPrice);
            order.getItems().add(orderItem);
            order.setTotalAmount(order.getTotalAmount()
                    .add(unitPrice.multiply(BigDecimal.valueOf(quantity))));
        }

        // Save the order and clear the cart
        Order savedOrder = orderRepository.save(order);
        cartItemRepository.deleteAll(cartItems);

        return toOrderResponseDto(savedOrder);
    }

    private OrderResponseDto toOrderResponseDto(Order order) {
        OrderResponseDto response = new OrderResponseDto();
        response.setId(order.getId());
        response.setTotalAmount(order.getTotalAmount());
        response.setStatus(order.getStatus());
        response.setCreatedAt(order.getCreatedAt());
        response.setItems(order.getItems().stream()
                .map(this::toOrderItemDto)
                .toList());
        return response;
    }

    private OrderItemDto toOrderItemDto(OrderItem item) {
        OrderItemDto response = new OrderItemDto();
        response.setId(item.getId());
        response.setProductId(item.getProduct().getId());
        response.setQuantity(item.getQuantity());
        response.setPrice(item.getPrice());
        return response;
    }
}
