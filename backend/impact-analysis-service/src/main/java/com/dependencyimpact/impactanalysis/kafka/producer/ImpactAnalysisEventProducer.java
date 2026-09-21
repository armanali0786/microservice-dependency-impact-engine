package com.dependencyimpact.impactanalysis.kafka.producer;

import com.dependencyimpact.common.events.EventEnvelope;
import com.dependencyimpact.common.events.EventType;
import com.dependencyimpact.impactanalysis.dto.ImpactAnalysisCompletedPayload;
import com.dependencyimpact.impactanalysis.dto.ImpactAnalysisFailedPayload;
import com.dependencyimpact.impactanalysis.dto.ImpactAnalysisRequestedPayload;
import com.dependencyimpact.impactanalysis.exception.ImpactAnalysisException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
public class ImpactAnalysisEventProducer {

    private static final String TOPIC = "impact-analysis-events";

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public ImpactAnalysisEventProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void publishRequested(ImpactAnalysisRequestedPayload payload, String environment) {
        publish(EventType.IMPACT_ANALYSIS_REQUESTED, payload, environment, payload.analysisId());
    }

    public void publishCompleted(ImpactAnalysisCompletedPayload payload, String environment) {
        publish(EventType.IMPACT_ANALYSIS_COMPLETED, payload, environment, payload.analysisId());
    }

    public void publishFailed(ImpactAnalysisFailedPayload payload, String environment) {
        publish(EventType.IMPACT_ANALYSIS_FAILED, payload, environment, payload.analysisId());
    }

    private void publish(EventType eventType, Object payload, String environment, UUID analysisId) {
        EventEnvelope<Object> envelope = new EventEnvelope<>(
                UUID.randomUUID().toString(),
                eventType,
                1,
                "impact-analysis-service",
                environment,
                Instant.now(),
                null,
                null,
                payload
        );

        try {
            String json = objectMapper.writeValueAsString(envelope);
            // Keying by analysisId means every event about the same analysis
            // (requested -> completed/failed) lands on the same partition, in order.
            kafkaTemplate.send(TOPIC, analysisId.toString(), json);
        } catch (Exception e) {
            throw new ImpactAnalysisException("Failed to publish " + eventType + " event: " + e.getMessage());
        }
    }
}
