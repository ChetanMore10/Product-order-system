package com.product_order_system.service;

import com.product_order_system.dto.request.CartItemRequest;
import com.product_order_system.dto.response.CartResponse;

public interface CartService {

    CartResponse getCart(Long userId);
    CartResponse addItem(Long userId, CartItemRequest request);
    CartResponse updateItem(Long userId, Long productId, Integer quantity);
    void removeItem(Long userId, Long productId);
    void clearCart(Long userId);
}