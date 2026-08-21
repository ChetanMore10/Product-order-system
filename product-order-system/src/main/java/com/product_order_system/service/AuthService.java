package com.product_order_system.service;

import com.product_order_system.dto.request.LoginRequest;
import com.product_order_system.dto.request.RegisterRequest;
import com.product_order_system.dto.response.LoginResponse;
import com.product_order_system.dto.response.UserResponse;

public interface AuthService {

    UserResponse register(RegisterRequest request);

    LoginResponse login(LoginRequest request);
}