package com.dependencyimpact.dependencycollector;

@org.springframework.boot.autoconfigure.SpringBootApplication
@org.springframework.boot.context.properties.EnableConfigurationProperties(com.dependencyimpact.common.security.JwtProperties.class)
@org.springframework.context.annotation.Import({
        com.dependencyimpact.common.security.JwtTokenProvider.class,
        com.dependencyimpact.common.security.JwtAuthenticationFilter.class,
        com.dependencyimpact.common.observability.CorrelationIdFilter.class
})
public class DependencyCollectorApplication {
    public static void main(String[] args) {
        org.springframework.boot.SpringApplication.run(DependencyCollectorApplication.class, args);
    }
}
