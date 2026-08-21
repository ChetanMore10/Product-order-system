package com.product_order_system.config;

import com.product_order_system.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable())

            .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth

                        // Swagger
                        .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**").permitAll()

                        // Authentication
                        .requestMatchers("/api/auth/**").permitAll()


                        // ========================================
                        // PUBLIC - USER CAN VIEW PRODUCTS
                        // ========================================

                        .requestMatchers("/api/products", "/api/products/", "/api/products/{id}", "/api/products/category/**").permitAll()


                        // ========================================
                        // SUPER_ADMIN - CATEGORY MANAGEMENT
                        // ========================================

                        .requestMatchers("/api/categories/**").hasRole("SUPER_ADMIN")


                        // ========================================
                        // ADMIN + SUPER_ADMIN
                        // PRODUCT MANAGEMENT
                        // INVENTORY MANAGEMENT
                        // ========================================

                        .requestMatchers("/api/products/**", "/api/inventory/**").hasAnyRole("ADMIN", "SUPER_ADMIN")


                        // ========================================
                        // USER + ADMIN + SUPER_ADMIN
                        // CART
                        // ADDRESS
                        // ORDERS
                        // ========================================

                        .requestMatchers("/api/cart/**", "/api/addresses/**", "/api/orders/checkout/**", "/api/orders/my-orders", "/api/orders/user/**").hasAnyRole("USER", "ADMIN", "SUPER_ADMIN")


                        // ========================================
                        // SUPER_ADMIN ONLY
                        // USER MANAGEMENT
                        // ROLE MANAGEMENT
                        // ALL ORDERS
                        // ========================================

                        .requestMatchers("/api/users/**", "/api/roles/**", "/api/orders/**").hasRole("SUPER_ADMIN")


                        // All other requests need authentication
                        .anyRequest().authenticated())

                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5174", "http://localhost:5175", "http://localhost:5176"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}