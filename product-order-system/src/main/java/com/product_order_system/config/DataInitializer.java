package com.product_order_system.config;

import com.product_order_system.entity.Role;
import com.product_order_system.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final RoleRepository roleRepository;

    @Bean
    public CommandLineRunner initializeRoles() {
        return args -> {

            if (!roleRepository.existsByName("USER")) {
                Role userRole = new Role();
                userRole.setName("USER");
                roleRepository.save(userRole);
            }

            if (!roleRepository.existsByName("ADMIN")) {
                Role adminRole = new Role();
                adminRole.setName("ADMIN");
                roleRepository.save(adminRole);
            }

            if (!roleRepository.existsByName("SUPER_ADMIN")) {
                Role superAdminRole = new Role();
                superAdminRole.setName("SUPER_ADMIN");
                roleRepository.save(superAdminRole);
            }

            System.out.println("Roles initialized successfully!");
        };
    }
}