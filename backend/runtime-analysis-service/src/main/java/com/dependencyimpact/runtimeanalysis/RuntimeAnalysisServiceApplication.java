package com.dependencyimpact.runtimeanalysis;

@org.springframework.boot.autoconfigure.SpringBootApplication
@org.springframework.scheduling.annotation.EnableScheduling
public class RuntimeAnalysisServiceApplication {
    public static void main(String[] args) {
        org.springframework.boot.SpringApplication.run(RuntimeAnalysisServiceApplication.class, args);
    }
}
