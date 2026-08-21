package com.product_order_system.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class UserResponse {

    private Long id;

    private String username;

    private String email;

    private Set<String> roles;
}