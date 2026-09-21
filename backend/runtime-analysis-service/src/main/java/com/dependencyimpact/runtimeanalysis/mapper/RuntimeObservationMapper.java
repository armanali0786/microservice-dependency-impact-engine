package com.dependencyimpact.runtimeanalysis.mapper;

import com.dependencyimpact.runtimeanalysis.dto.RuntimeObservationRequest;
import com.dependencyimpact.runtimeanalysis.dto.RuntimeObservationResponse;
import com.dependencyimpact.runtimeanalysis.entity.RuntimeObservation;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
public class RuntimeObservationMapper {

    public RuntimeObservation toEntity(RuntimeObservationRequest request) {
        RuntimeObservation observation = new RuntimeObservation();
        observation.setId(UUID.randomUUID());
        observation.setTraceId(request.getTraceId());
        observation.setSpanId(request.getSpanId());
        observation.setSourceServiceId(request.getSourceServiceId());
        observation.setTargetServiceId(request.getTargetServiceId());
        observation.setEndpoint(request.getEndpoint());
        observation.setHttpMethod(request.getHttpMethod());
        observation.setStatusCode(request.getStatusCode());
        observation.setLatencyMs(request.getLatencyMs());
        observation.setEnvironment(request.getEnvironment());
        observation.setObservedAt(request.getObservedAt() != null ? request.getObservedAt() : Instant.now());
        return observation;
    }

    public RuntimeObservationResponse toResponse(RuntimeObservation observation) {
        return new RuntimeObservationResponse(
                observation.getId(),
                observation.getTraceId(),
                observation.getSpanId(),
                observation.getSourceServiceId(),
                observation.getTargetServiceId(),
                observation.getEndpoint(),
                observation.getHttpMethod(),
                observation.getStatusCode(),
                observation.getLatencyMs(),
                observation.getObservedAt(),
                observation.getEnvironment()
        );
    }
}
