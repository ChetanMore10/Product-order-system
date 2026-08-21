package com.product_order_system.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class InventoryResponse {

    private Long id;

    private Long productId;

    private String productName;

    private Integer quantity;
}