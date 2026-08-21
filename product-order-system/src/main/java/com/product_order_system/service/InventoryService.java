package com.product_order_system.service;

import com.product_order_system.dto.request.InventoryRequest;
import com.product_order_system.dto.response.InventoryResponse;

public interface InventoryService {

    InventoryResponse getInventoryByProductId(Long productId);
    InventoryResponse updateInventory(Long productId, InventoryRequest request);
}