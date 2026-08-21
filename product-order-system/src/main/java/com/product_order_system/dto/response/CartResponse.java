package com.product_order_system.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CartResponse {

    private Long cartId;

    private Long userId;

    private List<CartItemResponse> items;

    private BigDecimal totalAmount;
}