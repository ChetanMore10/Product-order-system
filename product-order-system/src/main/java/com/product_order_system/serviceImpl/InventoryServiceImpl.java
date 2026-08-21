package com.product_order_system.serviceImpl;

import com.product_order_system.dto.request.InventoryRequest;
import com.product_order_system.dto.response.InventoryResponse;
import com.product_order_system.entity.Inventory;
import com.product_order_system.entity.Product;
import com.product_order_system.exception.ResourceNotFoundException;
import com.product_order_system.repository.InventoryRepository;
import com.product_order_system.repository.ProductRepository;
import com.product_order_system.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional(readOnly = true)
    public InventoryResponse getInventoryByProductId(Long productId) {

        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        Inventory inventory = inventoryRepository.findByProductId(productId).orElseThrow(() -> new ResourceNotFoundException("Inventory not found for product id: " + productId));

        return mapToResponse(inventory, product);
    }

    @Override
    public InventoryResponse updateInventory(Long productId, InventoryRequest request) {

        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        Inventory inventory = inventoryRepository.findByProductId(productId).orElseThrow(() -> new ResourceNotFoundException("Inventory not found for product id: " + productId));

        inventory.setQuantity(request.getQuantity());

        Inventory updatedInventory = inventoryRepository.save(inventory);

        return mapToResponse(updatedInventory, product);
    }

    private InventoryResponse mapToResponse(Inventory inventory, Product product) {

        return new InventoryResponse(
                inventory.getId(),
                product.getId(),
                product.getName(),
                inventory.getQuantity()
        );
    }
}