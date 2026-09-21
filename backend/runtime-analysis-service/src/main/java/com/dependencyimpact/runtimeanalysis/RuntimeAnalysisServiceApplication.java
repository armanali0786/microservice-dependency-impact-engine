package com.dependencyimpact.runtimeanalysis;

@org.springframework.boot.autoconfigure.SpringBootApplication
@org.springframework.scheduling.annotation.EnableScheduling
@org.springframework.boot.context.properties.EnableConfigurationProperties(com.dependencyimpact.common.security.JwtProperties.class)
@org.springframework.context.annotation.Import({
        com.dependencyimpact.common.security.JwtTokenProvider.class,
        com.dependencyimpact.common.security.JwtAuthenticationFilter.class,
        com.dependencyimpact.common.observability.CorrelationIdFilter.class
})
public class RuntimeAnalysisServiceApplication {
    public static void main(String[] args) {
        org.springframework.boot.SpringApplication.run(RuntimeAnalysisServiceApplication.class, args);
    }
}
