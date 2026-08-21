package com.product_order_system.service;

import com.product_order_system.dto.request.ProductRequest;
import com.product_order_system.dto.response.ProductResponse;

import java.util.List;

public interface ProductService {

    ProductResponse createProduct(ProductRequest request);
    ProductResponse getProductById(Long id);
    List<ProductResponse> getAllProducts();
    List<ProductResponse> getProductsByCategory(Long categoryId);
    ProductResponse updateProduct(Long id, ProductRequest request);
    void enableProduct(Long id);
    void disableProduct(Long id);
}