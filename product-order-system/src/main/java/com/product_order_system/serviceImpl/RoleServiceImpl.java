package com.product_order_system.serviceImpl;

import com.product_order_system.dto.response.UserResponse;
import com.product_order_system.entity.Role;
import com.product_order_system.entity.User;
import com.product_order_system.exception.ResourceNotFoundException;
import com.product_order_system.repository.RoleRepository;
import com.product_order_system.repository.UserRepository;
import com.product_order_system.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RoleServiceImpl implements RoleService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public UserResponse assignRole(Long userId, String roleName) {

        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Role role = roleRepository.findByName(roleName.toUpperCase()).orElseThrow(() -> new ResourceNotFoundException("Role not found: " + roleName));

        user.getRoles().add(role);

        User updatedUser = userRepository.save(user);

        return new UserResponse(updatedUser.getId(), updatedUser.getUsername(), updatedUser.getEmail(), updatedUser.getRoles().stream().map(Role::getName).collect(Collectors.toSet()));
    }
}