package com.dependencyimpact.runtimeanalysis.service;

import com.dependencyimpact.runtimeanalysis.dto.IncidentCreatedPayload;
import com.dependencyimpact.runtimeanalysis.dto.RuntimeMetricsResponse;
import com.dependencyimpact.runtimeanalysis.entity.Incident;
import com.dependencyimpact.runtimeanalysis.kafka.producer.IncidentEventProducer;
import com.dependencyimpact.runtimeanalysis.mapper.IncidentMapper;
import com.dependencyimpact.runtimeanalysis.repository.IncidentRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

// Fixed, explainable thresholds - same spirit as RiskClassificationService in
// impact-analysis-service: simple rules with a reason attached, not a scoring model.
// Tuning these, or making them per-service/configurable, is future work.
@Service
public class IncidentDetectionService {

    private static final double ERROR_RATE_THRESHOLD = 0.05;
    private static final double CRITICAL_ERROR_RATE_THRESHOLD = 0.20;
    private static final double P95_LATENCY_THRESHOLD_MS = 1000;

    private final IncidentRepository incidentRepository;
    private final IncidentMapper incidentMapper;
    private final IncidentEventProducer incidentEventProducer;

    public IncidentDetectionService(IncidentRepository incidentRepository, IncidentMapper incidentMapper,
                                     IncidentEventProducer incidentEventProducer) {
        this.incidentRepository = incidentRepository;
        this.incidentMapper = incidentMapper;
        this.incidentEventProducer = incidentEventProducer;
    }

    public Optional<Incident> evaluate(UUID serviceId, RuntimeMetricsResponse metrics) {
        if (metrics.getSampleCount() == 0) {
            return Optional.empty();
        }

        List<String> reasons = new ArrayList<>();
        if (metrics.getErrorRate() > ERROR_RATE_THRESHOLD) {
            reasons.add(String.format("error rate %.1f%% exceeds %.0f%% threshold",
                    metrics.getErrorRate() * 100, ERROR_RATE_THRESHOLD * 100));
        }
        if (metrics.getP95LatencyMs() > P95_LATENCY_THRESHOLD_MS) {
            reasons.add(String.format("p95 latency %.0fms exceeds %.0fms threshold",
                    metrics.getP95LatencyMs(), P95_LATENCY_THRESHOLD_MS));
        }
        if (reasons.isEmpty()) {
            return Optional.empty();
        }

        // Don't open a second incident for a service that already has one open -
        // that would just spam duplicates every time the scheduler ticks.
        if (!incidentRepository.findByServiceIdAndStatus(serviceId, "OPEN").isEmpty()) {
            return Optional.empty();
        }

        String severity = metrics.getErrorRate() > CRITICAL_ERROR_RATE_THRESHOLD ? "CRITICAL" : "HIGH";
        String title = "Elevated error rate or latency detected";
        String description = String.join("; ", reasons);

        Incident incident = incidentMapper.newAutoDetected(
                serviceId, title, description, severity, metrics.getErrorRate(), metrics.getP95LatencyMs());
        incidentRepository.save(incident);

        incidentEventProducer.publishCreated(
                new IncidentCreatedPayload(incident.getId(), serviceId, severity, title));

        return Optional.of(incident);
    }
}
