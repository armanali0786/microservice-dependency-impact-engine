package com.dependencyimpact.dependencycollector.controller;

import com.dependencyimpact.common.model.ApiResponse;
import com.dependencyimpact.common.model.DependencyConfidence;
import com.dependencyimpact.common.model.DependencyObservation;
import com.dependencyimpact.common.model.DependencyType;
import com.dependencyimpact.dependencycollector.dto.ManualDependencyObservationRequest;
import com.dependencyimpact.dependencycollector.kafka.producer.DependencyEventProducer;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequestMapping("/internal/dependency-events")
public class InternalDependencyEventController {

    private final DependencyEventProducer dependencyEventProducer;

    public InternalDependencyEventController(DependencyEventProducer dependencyEventProducer) {
        this.dependencyEventProducer = dependencyEventProducer;
    }

    // Trusted internal endpoint - stands in for the real OpenAPI/Kafka/DB/K8s
    // collectors, which all funnel into this same "publish an observation" step.
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> publish(@Valid @RequestBody ManualDependencyObservationRequest request) {
        DependencyObservation observation = new DependencyObservation(
                request.getSourceService(),
                request.getTargetService(),
                DependencyType.valueOf(request.getDependencyType()),
                request.getProtocol(),
                request.getEndpoint(),
                null,
                request.getEnvironment(),
                Instant.now(),
                request.getConfidence() != null ? DependencyConfidence.valueOf(request.getConfidence()) : DependencyConfidence.STATIC,
                null
        );

        dependencyEventProducer.publishDependencyDiscovered(observation);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ApiResponse<>(true, null, null, null));
    }
}
