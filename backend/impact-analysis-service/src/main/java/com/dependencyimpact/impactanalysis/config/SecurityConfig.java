package com.dependencyimpact.impactanalysis.config;

import com.dependencyimpact.common.security.JwtAuthenticationFilter;
import com.dependencyimpact.common.security.SecurityConfigSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Viewing impact reports is open to any authenticated role (VIEWER included,
 * per docs/security.md #11); starting a new analysis requires
 * DEVELOPER/ARCHITECT/SRE/ADMIN per docs/api-structure.md #86.
 */
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        return SecurityConfigSupport.buildResourceServerChain(http, jwtAuthenticationFilter, auth -> auth
                .requestMatchers(HttpMethod.GET, "/api/v1/**").authenticated()
                .requestMatchers("/api/v1/**").hasAnyRole("DEVELOPER", "ARCHITECT", "SRE", "ADMIN")
                .anyRequest().authenticated());
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return SecurityConfigSupport.noOpUserDetailsService();
    }
}
