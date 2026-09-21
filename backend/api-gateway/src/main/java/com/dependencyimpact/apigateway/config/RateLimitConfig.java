package com.dependencyimpact.apigateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Fixed-window request cap enforced by {@link com.dependencyimpact.apigateway.filter.RateLimitingFilter},
 * backed by Redis so the limit is shared across every gateway instance rather
 * than tracked per-JVM. See docs/security.md #19 (Rate Limiting).
 */
@Configuration
@EnableConfigurationProperties(RateLimitConfig.RateLimitProperties.class)
public class RateLimitConfig {

    @ConfigurationProperties(prefix = "gateway.rate-limit")
    public static class RateLimitProperties {
        /** Max requests allowed per key within one window. */
        private int limit = 100;
        /** Window length, in seconds. */
        private int windowSeconds = 60;

        public int getLimit() {
            return limit;
        }

        public void setLimit(int limit) {
            this.limit = limit;
        }

        public int getWindowSeconds() {
            return windowSeconds;
        }

        public void setWindowSeconds(int windowSeconds) {
            this.windowSeconds = windowSeconds;
        }
    }
}
