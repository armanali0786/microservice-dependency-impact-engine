package com.dependencyimpact.apigateway;

@org.springframework.boot.autoconfigure.SpringBootApplication
@org.springframework.boot.context.properties.EnableConfigurationProperties(com.dependencyimpact.common.security.JwtProperties.class)
@org.springframework.context.annotation.Import(com.dependencyimpact.common.security.JwtTokenProvider.class)
public class ApiGatewayApplication {
    public static void main(String[] args) {
        org.springframework.boot.SpringApplication.run(ApiGatewayApplication.class, args);
    }
}
