package com.dependencyimpact.notification.config;

import com.dependencyimpact.common.security.JwtAuthenticationFilter;
import com.dependencyimpact.common.security.SecurityConfigSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

/**
 * This service has no REST API of its own - just the WebSocket upgrade at
 * /ws/notifications. Browsers can't set an Authorization header on a WebSocket
 * handshake, so that path is left open here and authenticated instead by
 * {@link com.dependencyimpact.notification.websocket.WebSocketConfig}'s
 * handshake interceptor, which validates a `?token=` query parameter.
 */
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        return SecurityConfigSupport.buildResourceServerChain(http, jwtAuthenticationFilter, auth -> auth
                .requestMatchers("/ws/**").permitAll()
                .anyRequest().authenticated());
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return SecurityConfigSupport.noOpUserDetailsService();
    }
}
