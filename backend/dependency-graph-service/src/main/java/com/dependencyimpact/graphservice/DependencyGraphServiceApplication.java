package com.dependencyimpact.graphservice;

import com.dependencyimpact.graphservice.traversal.GraphTraversalProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@org.springframework.boot.autoconfigure.SpringBootApplication
@EnableConfigurationProperties(GraphTraversalProperties.class)
public class DependencyGraphServiceApplication {
    public static void main(String[] args) {
        org.springframework.boot.SpringApplication.run(DependencyGraphServiceApplication.class, args);
    }
}
