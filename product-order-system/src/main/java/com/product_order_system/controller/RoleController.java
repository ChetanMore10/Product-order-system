package com.product_order_system.controller;

import com.product_order_system.dto.response.UserResponse;
import com.product_order_system.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @PutMapping("/users/{userId}")
    public ResponseEntity<UserResponse> assignRole(
            @PathVariable Long userId,
            @RequestParam String roleName) {

        return ResponseEntity.ok(
                roleService.assignRole(
                        userId,
                        roleName
                )
        );
    }
}