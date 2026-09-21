package com.dependencyimpact.impactanalysis.mapper;

import com.dependencyimpact.common.model.DependencyCriticality;
import com.dependencyimpact.common.model.ImpactLevel;
import com.dependencyimpact.impactanalysis.dto.ImpactComponentResponse;
import com.dependencyimpact.impactanalysis.entity.ImpactComponent;
import com.dependencyimpact.impactanalysis.exception.ImpactAnalysisException;
import com.dependencyimpact.impactanalysis.service.TraversedComponent;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Component
public class ImpactComponentMapper {

    private final ObjectMapper objectMapper;

    public ImpactComponentMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public ImpactComponent toEntity(UUID analysisId, TraversedComponent traversed,
                                     ImpactLevel impactLevel, String reason, boolean runtimeEvidence) {
        ImpactComponent component = new ImpactComponent();
        component.setId(UUID.randomUUID());
        component.setImpactAnalysisId(analysisId);
        component.setServiceId(traversed.serviceId());
        component.setDependencyType(traversed.dependencyType() != null ? traversed.dependencyType().name() : null);
        component.setDepth(traversed.depth());
        component.setImpactLevel(impactLevel.name());
        component.setReason(reason);
        component.setDependencyPath(writePath(traversed.dependencyPath()));
        component.setRuntimeEvidence(runtimeEvidence);
        component.setCriticalDependency(traversed.criticality() == DependencyCriticality.CRITICAL);
        component.setCreatedAt(Instant.now());
        return component;
    }

    public ImpactComponentResponse toResponse(ImpactComponent component) {
        List<String> path = readPath(component.getDependencyPath());
        String serviceName = path.isEmpty() ? null : path.get(path.size() - 1);

        return new ImpactComponentResponse(
                component.getServiceId(),
                serviceName,
                component.getDepth(),
                component.getImpactLevel(),
                component.getReason(),
                component.getDependencyType(),
                Boolean.TRUE.equals(component.getCriticalDependency()),
                Boolean.TRUE.equals(component.getRuntimeEvidence()),
                path
        );
    }

    private String writePath(List<String> path) {
        try {
            return objectMapper.writeValueAsString(path);
        } catch (Exception e) {
            throw new ImpactAnalysisException("Failed to serialize dependency path: " + e.getMessage());
        }
    }

    private List<String> readPath(String json) {
        if (json == null) {
            return List.of();
        }
        try {
            return objectMapper.readValue(json, new TypeReference<List<String>>() {
            });
        } catch (Exception e) {
            throw new ImpactAnalysisException("Failed to deserialize dependency path: " + e.getMessage());
        }
    }
}
