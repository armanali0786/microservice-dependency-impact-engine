package com.dependencyimpact.apigateway.filter;

import com.dependencyimpact.apigateway.config.RateLimitConfig.RateLimitProperties;
import com.dependencyimpact.common.exceptions.ErrorCode;
import com.dependencyimpact.common.security.UserPrincipal;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;
import java.time.Duration;

/**
 * Fixed-window request cap shared across every gateway instance via Redis
 * (docs/security.md #19). Keyed by authenticated user id when the request
 * carries a valid JWT (see {@link JwtAuthenticationGatewayFilter}), otherwise
 * by client IP - so the unauthenticated /auth/login path still gets its own
 * effective limit per caller.
 */
@Component
public class RateLimitingFilter implements GlobalFilter, Ordered {

    private static final String KEY_PREFIX = "gateway:rate-limit:";

    private final ReactiveStringRedisTemplate redisTemplate;
    private final RateLimitProperties properties;

    public RateLimitingFilter(ReactiveStringRedisTemplate redisTemplate, RateLimitProperties properties) {
        this.redisTemplate = redisTemplate;
        this.properties = properties;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String key = KEY_PREFIX + resolveClientKey(exchange);
        Duration window = Duration.ofSeconds(properties.getWindowSeconds());

        return redisTemplate.opsForValue().increment(key)
                .flatMap(count -> {
                    Mono<Void> proceedOrExpire = count == 1
                            ? redisTemplate.expire(key, window).then(Mono.empty())
                            : Mono.empty();

                    if (count > properties.getLimit()) {
                        return proceedOrExpire.then(GatewayResponseWriter.writeError(exchange.getResponse(),
                                HttpStatus.TOO_MANY_REQUESTS, ErrorCode.RATE_LIMIT_EXCEEDED,
                                "Rate limit exceeded. Please slow down and try again shortly."));
                    }
                    return proceedOrExpire.then(chain.filter(exchange));
                })
                // If Redis itself is unavailable, fail open rather than blocking all traffic.
                .onErrorResume(ex -> chain.filter(exchange));
    }

    private String resolveClientKey(ServerWebExchange exchange) {
        Object principal = exchange.getAttribute(JwtAuthenticationGatewayFilter.PRINCIPAL_ATTRIBUTE);
        if (principal instanceof UserPrincipal userPrincipal) {
            return "user:" + userPrincipal.getId();
        }

        ServerHttpRequest request = exchange.getRequest();
        InetSocketAddress remoteAddress = request.getRemoteAddress();
        String ip = remoteAddress != null && remoteAddress.getAddress() != null
                ? remoteAddress.getAddress().getHostAddress()
                : "unknown";
        return "ip:" + ip;
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 20;
    }
}
