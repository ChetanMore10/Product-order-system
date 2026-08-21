package com.product_order_system.serviceImpl;

import com.product_order_system.dto.response.UserResponse;
import com.product_order_system.entity.User;
import com.product_order_system.exception.ResourceNotFoundException;
import com.product_order_system.repository.UserRepository;
import com.product_order_system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {

        return userRepository.findAll().stream().map(this::mapToResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {

        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        return mapToResponse(user);
    }

    @Override
    public void deleteUser(Long id) {

        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        userRepository.delete(user);
    }

    private UserResponse mapToResponse(User user) {

        return new UserResponse(user.getId(), user.getUsername(), user.getEmail(), user.getRoles().stream().map(role -> role.getName()).collect(java.util.stream.Collectors.toSet()));
    }
}