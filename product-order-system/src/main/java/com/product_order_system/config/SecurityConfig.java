package com.product_order_system.config;

import com.product_order_system.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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

        http
                // ========================================
                // CSRF
                // ========================================
                .csrf(csrf -> csrf.disable())

                // ========================================
                // CORS
                // ========================================
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // ========================================
                // STATELESS SESSION - JWT
                // ========================================
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // ========================================
                // AUTHORIZATION
                // ========================================
                .authorizeHttpRequests(auth -> auth

                        // ----------------------------------------
                        // CORS PREFLIGHT
                        // ----------------------------------------
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()


                        // ----------------------------------------
                        // SWAGGER
                        // ----------------------------------------
                        .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**").permitAll()


                        // ----------------------------------------
                        // AUTHENTICATION
                        // Login / Register
                        // ----------------------------------------
                        .requestMatchers("/api/auth/**").permitAll()


                        // ----------------------------------------
                        // PUBLIC PRODUCTS - GET ONLY
                        // Anyone can view products
                        // ----------------------------------------
                        .requestMatchers(HttpMethod.GET, "/api/products", "/api/products/", "/api/products/*", "/api/products/category/**").permitAll()


                        // ========================================
                        // CATEGORY PERMISSIONS
                        // ========================================

                        // USER + ADMIN + SUPER_ADMIN
                        // can view categories
                        .requestMatchers(HttpMethod.GET, "/api/categories/**").hasAnyRole("USER", "ADMIN", "SUPER_ADMIN")

                        // SUPER_ADMIN only
                        // create category
                        .requestMatchers(HttpMethod.POST, "/api/categories/**").hasRole("SUPER_ADMIN")

                        // SUPER_ADMIN only
                        // update category
                        .requestMatchers(HttpMethod.PUT, "/api/categories/**").hasRole("SUPER_ADMIN")

                        // SUPER_ADMIN only
                        // delete category
                        .requestMatchers(HttpMethod.DELETE, "/api/categories/**").hasRole("SUPER_ADMIN")


                        // ========================================
                        // PRODUCT MANAGEMENT
                        // ADMIN + SUPER_ADMIN
                        // ========================================

                        .requestMatchers("/api/products/**", "/api/inventory/**").hasAnyRole("ADMIN", "SUPER_ADMIN")


                        // ========================================
                        // CART
                        // ADDRESS
                        // CHECKOUT
                        // MY ORDERS
                        // ========================================

                        .requestMatchers("/api/cart/**", "/api/addresses/**", "/api/orders/checkout", "/api/orders/checkout/**", "/api/orders/my-orders", "/api/orders/user/**").hasAnyRole("USER", "ADMIN", "SUPER_ADMIN")


                        // ========================================
                        // SUPER_ADMIN ONLY
                        // ========================================

                        .requestMatchers("/api/users/**", "/api/roles/**", "/api/orders/**").hasRole("SUPER_ADMIN")


                        // ========================================
                        // EVERYTHING ELSE
                        // ========================================

                        .anyRequest().authenticated())

                // ========================================
                // JWT FILTER
                // ========================================
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // ========================================
    // CORS CONFIGURATION
    // ========================================
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}