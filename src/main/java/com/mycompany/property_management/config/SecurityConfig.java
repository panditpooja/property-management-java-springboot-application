package com.mycompany.property_management.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. Disable CSRF (common for stateless REST APIs)
                .csrf(csrf -> csrf.disable())

                // 2. Allow frames for h2-console (Same Origin)
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                )

                // 3. Define authorization rules
                .authorizeHttpRequests(authz -> authz
                        // Public endpoints - no authentication required
                        .requestMatchers("/api/v1/users/registerUser", "/api/v1/users/loginUser").permitAll()

                        // Admin only endpoints
                        .requestMatchers("/api/v1/properties/updateProperties/**").hasRole("ADMIN")

                        // Property Owner and Admin can add properties
                        .requestMatchers("/api/v1/properties/addProperty", "/api/v1/properties/addProperties")
                        .hasAnyRole("ADMIN", "PROPERTY_OWNER")

                        // Property Owner and Admin can delete properties
                        .requestMatchers("/api/v1/properties/deleteProperty/**")
                        .hasAnyRole("ADMIN", "PROPERTY_OWNER")

                        // All authenticated users can view and search properties
                        // (Ownership checks for /getProperties/users/** are handled in PropertyService)
                        .requestMatchers("/api/v1/properties/getProperties",
                                "/api/v1/properties/getProperty/**",
                                "/api/v1/properties/getProperties/users/**",
                                "/api/v1/properties/search/**").authenticated()

                        // All other API endpoints require authentication
                        .requestMatchers("/api/v1/**").authenticated()

                        // Allow all other requests (like Swagger UI, h2-console)
                        .anyRequest().permitAll()
                )

                // 4. Make the session stateless (no cookies, JWT only)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // 5. Tell Spring to use your JWT filter
                // This runs YOUR filter before Spring's default login page filter
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

