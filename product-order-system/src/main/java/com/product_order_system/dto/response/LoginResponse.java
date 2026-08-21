package com.product_order_system.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class LoginResponse {

    private String token;

    private Long userId;

    private String username;

    private Set<String> roles;
}