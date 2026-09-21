package com.dependencyimpact.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * Actual JWT validation and RBAC live in
 * {@link com.dependencyimpact.apigateway.filter.JwtAuthenticationGatewayFilter},
 * which runs as a Spring Cloud Gateway {@code GlobalFilter} so the same code
 * path handles both routed proxy requests and any locally-defined endpoints.
 * This config just switches off Spring Security's own defaults (form login,
 * HTTP Basic, CSRF) so they don't interfere with that filter or with the
 * downstream services' own JWT checks (defense in depth, docs/security.md #23).
 */
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .authorizeExchange(exchange -> exchange.anyExchange().permitAll())
                .build();
    }
}
