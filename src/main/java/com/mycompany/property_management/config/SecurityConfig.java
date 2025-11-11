package com.mycompany.property_management.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

//    @Autowired
//    private JwtRequestFilter jwtRequestFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. Disable CSRF (common for stateless REST APIs)
                .csrf(csrf -> csrf.disable())

                // 2. Define authorization rules
                .authorizeHttpRequests(authz -> authz
                        // Allow your login and registration endpoints to be public
                        .requestMatchers("/api/v1/users/register", "/api/v1/users/login").permitAll()

//                        .requestMatchers("/api/v1/properties/addProperty", "/api/v1/properties/addProperties").authenticated()

                                // Secure all other endpoints (for now)
                                // .anyRequest().authenticated()

                        // OR... if you want to allow all your API endpoints for testing:
                        .requestMatchers("/api/v1/**").permitAll() // Allows everything under /api/v1/
                        .anyRequest().permitAll() // Allow all other requests (like root)
                );
//                // --- THIS IS THE NEW PART ---
//
//                // 4. Make the session stateless (no cookies)
//                    .sessionManagement(session -> session
//                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                )
//
//                // 5. Tell Spring to use your JWT filter
//                // This runs YOUR filter before Spring's default login page filter
//                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

