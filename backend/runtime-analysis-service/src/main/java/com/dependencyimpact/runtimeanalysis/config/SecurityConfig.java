package com.dependencyimpact.runtimeanalysis.config;

import com.dependencyimpact.common.security.JwtAuthenticationFilter;
import com.dependencyimpact.common.security.SecurityConfigSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Incidents are readable by any authenticated role (docs/security.md #11 lists
 * "View incidents" under VIEWER); modifying incident state or triggering
 * blast-radius analysis is SRE/ADMIN only ("Modify Incidents", #12).
 * Raw runtime telemetry (observations/metrics) is restricted to SRE/ADMIN,
 * matching "View runtime telemetry" being an SRE-only responsibility.
 */
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        return SecurityConfigSupport.buildResourceServerChain(http, jwtAuthenticationFilter, auth -> auth
                .requestMatchers(HttpMethod.GET, "/api/v1/incidents/**").authenticated()
                .requestMatchers("/api/v1/incidents/**").hasAnyRole("SRE", "ADMIN")
                .requestMatchers("/api/v1/runtime/**").hasAnyRole("SRE", "ADMIN")
                .anyRequest().authenticated());
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return SecurityConfigSupport.noOpUserDetailsService();
    }
}
