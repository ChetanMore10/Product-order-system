package com.product_order_system.serviceImpl;

import com.product_order_system.dto.request.LoginRequest;
import com.product_order_system.dto.request.RegisterRequest;
import com.product_order_system.dto.response.LoginResponse;
import com.product_order_system.dto.response.UserResponse;
import com.product_order_system.entity.Role;
import com.product_order_system.entity.User;
import com.product_order_system.exception.DuplicateResourceException;
import com.product_order_system.exception.ResourceNotFoundException;
import com.product_order_system.repository.RoleRepository;
import com.product_order_system.repository.UserRepository;
import com.product_order_system.security.JwtService;
import com.product_order_system.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    @Override
    public UserResponse register(RegisterRequest request) {

        // Check username
        if (userRepository.existsByUsername(request.getUsername())) {

            throw new DuplicateResourceException("Username already exists: " + request.getUsername());
        }

        // Check email
        if (userRepository.existsByEmail(request.getEmail())) {

            throw new DuplicateResourceException("Email already exists: " + request.getEmail());
        }

        // Find default USER role
        Role userRole = roleRepository.findByName("USER").orElseThrow(() -> new ResourceNotFoundException("USER role not found"));

        // Create user
        User user = new User();

        user.setUsername(request.getUsername());

        user.setEmail(request.getEmail());

        // Encrypt password
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Assign USER role
        user.setRoles(Set.of(userRole));

        User savedUser = userRepository.save(user);

        return mapToUserResponse(savedUser);
    }

    @Override
    public LoginResponse login(LoginRequest request) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());

        String token = jwtService.generateToken(userDetails);

        User user = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Set<String> roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toSet());

        return new LoginResponse(token, user.getId(), user.getUsername(), roles);
    }

    private UserResponse mapToUserResponse(User user) {

        Set<String> roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toSet());

        return new UserResponse(user.getId(), user.getUsername(), user.getEmail(), roles);
    }
}