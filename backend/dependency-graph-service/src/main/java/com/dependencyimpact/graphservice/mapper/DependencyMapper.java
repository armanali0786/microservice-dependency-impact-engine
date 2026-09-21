package com.dependencyimpact.graphservice.mapper;

import com.dependencyimpact.graphservice.dto.DependencyResponse;
import com.dependencyimpact.graphservice.entity.Dependency;
import org.springframework.stereotype.Component;

@Component
public class DependencyMapper {

    public DependencyResponse toResponse(Dependency dependency) {
        return new DependencyResponse(
                dependency.getId(),
                dependency.getSourceServiceId(),
                dependency.getTargetServiceId(),
                dependency.getDependencyType(),
                dependency.getProtocol(),
                dependency.getEndpoint(),
                dependency.getTopicName(),
                dependency.getEnvironment(),
                dependency.getConfidence(),
                dependency.getStatus(),
                dependency.getFirstSeenAt(),
                dependency.getLastSeenAt()
        );
    }
}
