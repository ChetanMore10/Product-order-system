package com.product_order_system.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class OrderResponse {

    private Long id;

    private Long userId;

    private String username;

    private Long addressId;

    private BigDecimal totalAmount;

    private String status;

    private LocalDateTime createdAt;

    private List<OrderItemResponse> items;
}