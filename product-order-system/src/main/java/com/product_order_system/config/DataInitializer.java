package com.product_order_system.config;

import com.product_order_system.entity.Role;
import com.product_order_system.entity.User;
import com.product_order_system.repository.RoleRepository;
import com.product_order_system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initializeData() {

        return args -> {

            // ========================================
            // CREATE / GET USER ROLE
            // ========================================

            Role userRole = roleRepository.findByName("USER").orElseGet(() -> {
                Role role = new Role();
                role.setName("USER");
                return roleRepository.save(role);
            });


            // ========================================
            // CREATE / GET ADMIN ROLE
            // ========================================

            Role adminRole = roleRepository.findByName("ADMIN").orElseGet(() -> {
                Role role = new Role();
                role.setName("ADMIN");
                return roleRepository.save(role);
            });


            // ========================================
            // CREATE / GET SUPER ADMIN ROLE
            // ========================================

            Role superAdminRole = roleRepository.findByName("SUPER_ADMIN").orElseGet(() -> {
                Role role = new Role();
                role.setName("SUPER_ADMIN");
                return roleRepository.save(role);
            });


            // ========================================
            // ADMIN USER
            // Username: admin
            // Password: Admin@123
            // Role: ADMIN
            // ========================================

            User admin = userRepository.findByUsername("admin").orElse(null);

            if (admin == null) {

                admin = new User();

                admin.setUsername("admin");
                admin.setEmail("admin@gmail.com");

                System.out.println("Creating admin user...");
            }

            admin.setPassword(passwordEncoder.encode("Admin@123"));

            admin.setRoles(Set.of(adminRole));

            userRepository.save(admin);

            System.out.println("Admin user ready: admin / Admin@123");


            // ========================================
            // SUPER ADMIN USER
            // Username: superadmin
            // Password: SuperAdmin@123
            // Role: SUPER_ADMIN
            // ========================================

            User superAdmin = userRepository.findByUsername("superadmin").orElse(null);

            if (superAdmin == null) {

                superAdmin = new User();

                superAdmin.setUsername("superadmin");
                superAdmin.setEmail("superadmin@gmail.com");

                System.out.println("Creating superadmin user...");
            }

            superAdmin.setPassword(passwordEncoder.encode("SuperAdmin@123"));

            superAdmin.setRoles(Set.of(superAdminRole));

            userRepository.save(superAdmin);

            System.out.println("Super admin ready: superadmin / SuperAdmin@123");


            // ========================================
            // COMPLETED
            // ========================================
            System.out.println("Roles and default users initialized successfully!");
        };
    }
}