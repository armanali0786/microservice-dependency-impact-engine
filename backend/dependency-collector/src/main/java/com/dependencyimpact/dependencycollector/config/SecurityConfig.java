package com.dependencyimpact.dependencycollector.config;

import com.dependencyimpact.common.security.JwtAuthenticationFilter;
import com.dependencyimpact.common.security.SecurityConfigSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

/**
 * /internal/dependency-events is not exposed through the gateway (see
 * api-gateway's GatewayRouteConfig) - it's meant for trusted internal
 * ingestion only (docs/api-structure.md #79). Requiring authentication here
 * too is defense in depth in case this port is ever reached directly.
 */
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        return SecurityConfigSupport.buildResourceServerChain(http, jwtAuthenticationFilter, auth -> auth
                .anyRequest().authenticated());
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return SecurityConfigSupport.noOpUserDetailsService();
    }
}
