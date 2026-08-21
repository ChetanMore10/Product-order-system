package com.product_order_system.controller;

import com.product_order_system.dto.request.CartItemRequest;
import com.product_order_system.dto.response.CartResponse;
import com.product_order_system.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<CartResponse> getCart(@RequestParam Long userId) {

        return ResponseEntity.ok(cartService.getCart(userId));
    }

    @PostMapping("/items")
    public ResponseEntity<CartResponse> addItem(@RequestParam Long userId, @Valid @RequestBody CartItemRequest request) {

        return ResponseEntity.ok(cartService.addItem(userId, request));
    }

    @PutMapping("/items/{productId}")
    public ResponseEntity<CartResponse> updateItem(@RequestParam Long userId, @PathVariable Long productId, @RequestParam Integer quantity) {

        return ResponseEntity.ok(cartService.updateItem(userId, productId, quantity));
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<Void> removeItem(
            @RequestParam Long userId,
            @PathVariable Long productId) {

        cartService.removeItem(
                userId,
                productId
        );

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> clearCart(
            @RequestParam Long userId) {

        cartService.clearCart(userId);

        return ResponseEntity.noContent().build();
    }
}