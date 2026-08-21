package com.product_order_system.serviceImpl;

import com.product_order_system.dto.request.ProductRequest;
import com.product_order_system.dto.response.ProductResponse;
import com.product_order_system.entity.Category;
import com.product_order_system.entity.Inventory;
import com.product_order_system.entity.Product;
import com.product_order_system.exception.ResourceNotFoundException;
import com.product_order_system.repository.CategoryRepository;
import com.product_order_system.repository.InventoryRepository;
import com.product_order_system.repository.ProductRepository;
import com.product_order_system.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final InventoryRepository inventoryRepository;

    @Override
    public ProductResponse createProduct(ProductRequest request) {

        // 1. Find category
        Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + request.getCategoryId()));

        // 2. Create product
        Product product = new Product();

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setCategory(category);
        product.setActive(true);

        // 3. Save product
        Product savedProduct = productRepository.save(product);

        // 4. Create inventory
        Inventory inventory = new Inventory();

        inventory.setProduct(savedProduct);

        inventory.setQuantity(request.getInventory() == null ? 0 : request.getInventory());

        // 5. Save inventory
        Inventory savedInventory = inventoryRepository.save(inventory);

        // 6. Return response
        return mapToResponse(savedProduct, savedInventory);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        Inventory inventory = inventoryRepository.findByProductId(id).orElse(null);
        return mapToResponse(product, inventory);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts() {

        return productRepository.findByActiveTrue().stream().map(product -> {
            Inventory inventory = inventoryRepository.findByProductId(product.getId()).orElse(null);
            return mapToResponse(product, inventory);
        }).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getProductsByCategory(Long categoryId) {

        // Verify category exists
        if (!categoryRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException("Category not found with id: " + categoryId);
        }

        return productRepository.findByCategoryIdAndActiveTrue(categoryId).stream().map(product -> {
            Inventory inventory = inventoryRepository.findByProductId(product.getId()).orElse(null);
            return mapToResponse(product, inventory);
        }).toList();
    }

    @Override
    public ProductResponse updateProduct(Long id, ProductRequest request) {

        Product product = productRepository
                .findById(id).orElseThrow(() ->
                        new ResourceNotFoundException("Product not found with id: " + id));

        Category category =
                categoryRepository.findById(request
                        .getCategoryId()).orElseThrow(() ->
                                new ResourceNotFoundException("Category not found with id: " + request.getCategoryId()));

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setCategory(category);

        Product updatedProduct = productRepository.save(product);
        Inventory inventory =
                inventoryRepository.findByProductId(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Inventory not found for product id: " + id));
        if (request.getInventory() != null) {
            inventory.setQuantity(request.getInventory());
            inventoryRepository.save(inventory);
        }
        return mapToResponse(updatedProduct, inventory);
    }

    @Override
    public void enableProduct(Long id) {
        Product product =
                productRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Product not found with id: " + id));
        product.setActive(true);
        productRepository.save(product);
    }

    @Override
    public void disableProduct(Long id) {

        Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        product.setActive(false);
        productRepository.save(product);
    }

    private ProductResponse mapToResponse(Product product, Inventory inventory) {

        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.isActive(),
                product.getCategory().getId(),
                product.getCategory().getName(),
                inventory != null
                        ? inventory.getQuantity()
                        : 0
        );
    }
}