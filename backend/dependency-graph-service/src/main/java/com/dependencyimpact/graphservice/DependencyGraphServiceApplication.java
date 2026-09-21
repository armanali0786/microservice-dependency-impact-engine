package com.dependencyimpact.graphservice;

import com.dependencyimpact.graphservice.traversal.GraphTraversalProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@org.springframework.boot.autoconfigure.SpringBootApplication
@EnableConfigurationProperties({GraphTraversalProperties.class, com.dependencyimpact.common.security.JwtProperties.class})
@org.springframework.context.annotation.Import({
        com.dependencyimpact.common.security.JwtTokenProvider.class,
        com.dependencyimpact.common.security.JwtAuthenticationFilter.class,
        com.dependencyimpact.common.observability.CorrelationIdFilter.class
})
public class DependencyGraphServiceApplication {
    public static void main(String[] args) {
        org.springframework.boot.SpringApplication.run(DependencyGraphServiceApplication.class, args);
    }
}
