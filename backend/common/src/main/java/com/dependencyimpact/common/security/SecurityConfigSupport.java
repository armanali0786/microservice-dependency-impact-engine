package com.dependencyimpact.common.security;

import com.dependencyimpact.common.exceptions.ErrorCode;
import com.dependencyimpact.common.model.ApiError;
import com.dependencyimpact.common.model.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Shared building blocks for every servlet-based service's SecurityConfig, so the
 * stateless-JWT-resource-server wiring (CSRF/session policy/JWT filter/401-403 JSON
 * bodies) isn't hand-duplicated across 6 modules. Each service still owns its own
 * authorization rules via the {@code authorizeCustomizer} it passes in.
 */
public final class SecurityConfigSupport {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private SecurityConfigSupport() {
    }

    public static SecurityFilterChain buildResourceServerChain(
            HttpSecurity http,
            JwtAuthenticationFilter jwtAuthenticationFilter,
            Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry> authorizeCustomizer)
            throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/actuator/health", "/actuator/info").permitAll();
                    authorizeCustomizer.customize(auth);
                })
                .exceptionHandling(handling -> handling
                        .authenticationEntryPoint((request, response, authException) ->
                                writeError(response, 401, ErrorCode.AUTHENTICATION_REQUIRED, "Authentication is required."))
                        .accessDeniedHandler((request, response, accessDeniedException) ->
                                writeError(response, 403, ErrorCode.ACCESS_DENIED, "You do not have permission to perform this action.")))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable);

        return http.build();
    }

    /**
     * JWT-only services don't authenticate against a Spring Security UserDetailsService,
     * but Spring Boot auto-configures one with a random generated password whenever no
     * bean is present. Registering an empty one suppresses that irrelevant warning.
     */
    public static UserDetailsService noOpUserDetailsService() {
        return new InMemoryUserDetailsManager();
    }

    private static void writeError(jakarta.servlet.http.HttpServletResponse response, int status,
            ErrorCode code, String message) throws java.io.IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        ApiResponse<Void> body = new ApiResponse<>(false, null, new ApiError(code.name(), message, null), null);
        response.getWriter().write(OBJECT_MAPPER.writeValueAsString(body));
    }
}
