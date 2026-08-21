package com.product_order_system.service;

import com.product_order_system.dto.response.UserResponse;

public interface RoleService {

    UserResponse assignRole(Long userId, String roleName);
}