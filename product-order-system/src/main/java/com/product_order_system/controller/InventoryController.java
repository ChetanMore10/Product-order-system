package com.product_order_system.controller;

import com.product_order_system.dto.request.InventoryRequest;
import com.product_order_system.dto.response.InventoryResponse;
import com.product_order_system.service.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping("/{productId}")
    public ResponseEntity<InventoryResponse> getInventory(@PathVariable Long productId) {
        return ResponseEntity.ok(inventoryService.getInventoryByProductId(productId));
    }

    @PutMapping("/{productId}")
    public ResponseEntity<InventoryResponse> updateInventory(@PathVariable Long productId, @Valid @RequestBody InventoryRequest request) {

        return ResponseEntity.ok(inventoryService.updateInventory(productId, request));
    }
}