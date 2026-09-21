package com.dependencyimpact.apigateway.filter;

import com.dependencyimpact.common.exceptions.ErrorCode;
import com.dependencyimpact.common.security.JwtTokenProvider;
import com.dependencyimpact.common.security.UserPrincipal;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Fast-fails unauthenticated requests at the edge before they reach a
 * backend service (docs/security.md #4's "JWT Validation" step in the
 * gateway). The Authorization header is passed through unchanged - each
 * backend service validates the token again itself, per the "Zero Trust
 * Between Services" principle (docs/security.md #2), so this is a
 * performance/DoS optimization, not the only line of defense.
 *
 * <p>WebSocket handshakes (/ws/**) are exempt: browsers cannot set a custom
 * header on a WS upgrade request, so that token travels as a query
 * parameter instead and is validated by notification-service's own
 * handshake interceptor.
 */
@Component
public class JwtAuthenticationGatewayFilter implements GlobalFilter, Ordered {

    private static final List<String> PUBLIC_PATHS = List.of(
            "/api/v1/auth/login",
            "/actuator/**",
            "/ws/**"
    );

    private static final String BEARER_PREFIX = "Bearer ";

    /** Exchange attribute key {@link RateLimitingFilter} reads to rate-limit by user instead of IP. */
    public static final String PRINCIPAL_ATTRIBUTE = "gateway.authenticatedPrincipal";

    private final JwtTokenProvider jwtTokenProvider;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    public JwtAuthenticationGatewayFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().value();
        if (PUBLIC_PATHS.stream().anyMatch(pattern -> pathMatcher.match(pattern, path))) {
            return chain.filter(exchange);
        }

        ServerHttpRequest request = exchange.getRequest();
        String header = request.getHeaders().getFirst("Authorization");

        if (header == null || !header.startsWith(BEARER_PREFIX)) {
            return GatewayResponseWriter.writeError(exchange.getResponse(), HttpStatus.UNAUTHORIZED,
                    ErrorCode.AUTHENTICATION_REQUIRED, "Authentication is required.");
        }

        String token = header.substring(BEARER_PREFIX.length());
        return jwtTokenProvider.validateAndParse(token)
                .<Mono<Void>>map(principal -> {
                    exchange.getAttributes().put(PRINCIPAL_ATTRIBUTE, principal);
                    return chain.filter(exchange);
                })
                .orElseGet(() -> GatewayResponseWriter.writeError(exchange.getResponse(), HttpStatus.UNAUTHORIZED,
                        ErrorCode.INVALID_TOKEN, "The provided token is invalid or has expired."));
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 10;
    }
}
