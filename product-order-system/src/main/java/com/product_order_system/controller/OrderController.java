package com.product_order_system.controller;

import com.product_order_system.dto.request.CheckoutRequest;
import com.product_order_system.dto.response.OrderResponse;
import com.product_order_system.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/checkout")
    public ResponseEntity<OrderResponse> checkout(@RequestParam Long userId, @Valid @RequestBody CheckoutRequest request) {

        return ResponseEntity.ok(orderService.checkout(userId, request));
    }

    @GetMapping("/my-orders")
    public ResponseEntity<List<OrderResponse>> getMyOrders(@RequestParam Long userId) {

        return ResponseEntity.ok(orderService.getMyOrders(userId));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderById(@RequestParam Long userId, @PathVariable Long orderId) {

        return ResponseEntity.ok(orderService.getOrderById(userId, orderId));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {

        return ResponseEntity.ok(orderService.getAllOrders());
    }
}