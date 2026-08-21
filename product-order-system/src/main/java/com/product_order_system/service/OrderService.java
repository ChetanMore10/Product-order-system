package com.product_order_system.service;

import com.product_order_system.dto.request.CheckoutRequest;
import com.product_order_system.dto.response.OrderResponse;

import java.util.List;

public interface OrderService {

    OrderResponse checkout(Long userId, CheckoutRequest request);

    OrderResponse getOrderById(Long userId, Long orderId);

    List<OrderResponse> getMyOrders(Long userId);

    List<OrderResponse> getAllOrders();
}
