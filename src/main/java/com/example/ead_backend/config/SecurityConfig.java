package com.example.ead_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable) // Disable CSRF for testing
            .authorizeHttpRequests(auth -> auth
                // Allow ALL requests without authentication (for testing)
                .anyRequest().permitAll()
            )
            .httpBasic(AbstractHttpConfigurer::disable); // Disable Basic Auth for now
        
        return http.build();
    }
}
