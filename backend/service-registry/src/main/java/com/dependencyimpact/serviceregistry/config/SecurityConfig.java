package com.dependencyimpact.serviceregistry.config;

import com.dependencyimpact.common.security.JwtAuthenticationFilter;
import com.dependencyimpact.common.security.SecurityConfigSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Authorization rules per docs/security.md #12 and docs/api-structure.md #86:
 * everyone (once authenticated) can read services/teams/dashboard/search;
 * only ARCHITECT/ADMIN can create or modify services and teams; only ADMIN
 * manages users and audit logs.
 */
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        return SecurityConfigSupport.buildResourceServerChain(http, jwtAuthenticationFilter, auth -> auth
                .requestMatchers("/api/v1/auth/login").permitAll()
                .requestMatchers("/api/v1/auth/me").authenticated()
                .requestMatchers("/api/v1/users/**").hasRole("ADMIN")
                .requestMatchers("/api/v1/audit-logs/**").hasRole("ADMIN")
                .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/v1/**").authenticated()
                .requestMatchers("/api/v1/teams/**", "/api/v1/services/**")
                        .hasAnyRole("ARCHITECT", "ADMIN")
                .anyRequest().authenticated());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return SecurityConfigSupport.noOpUserDetailsService();
    }
}
