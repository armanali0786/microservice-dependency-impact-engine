package com.dependencyimpact.impactanalysis.kafka.consumer;

import com.dependencyimpact.common.events.EventEnvelope;
import com.dependencyimpact.common.exceptions.NonRetryableEventException;
import com.dependencyimpact.impactanalysis.dto.ImpactAnalysisRequestedPayload;
import com.dependencyimpact.impactanalysis.entity.ProcessedEvent;
import com.dependencyimpact.impactanalysis.repository.ProcessedEventRepository;
import com.dependencyimpact.impactanalysis.service.ImpactWorker;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.Instant;

// impact-analysis-events carries this service's own full lifecycle (requested,
// completed, failed) on one topic, per docs/kafka-spec.md section 19-21. That means
// this consumer also sees the COMPLETED/FAILED events it published itself further
// down the pipeline, and must ignore anything that isn't a fresh request - otherwise
// it would try to "process" its own result events as if they were new work.
@Component
public class ImpactAnalysisRequestConsumer {

    private static final Logger log = LoggerFactory.getLogger(ImpactAnalysisRequestConsumer.class);
    private static final String CONSUMER_GROUP = "impact-analysis-consumers";

    private final ObjectMapper objectMapper;
    private final ProcessedEventRepository processedEventRepository;
    private final ImpactWorker impactWorker;

    public ImpactAnalysisRequestConsumer(ObjectMapper objectMapper,
                                          ProcessedEventRepository processedEventRepository,
                                          ImpactWorker impactWorker) {
        this.objectMapper = objectMapper;
        this.processedEventRepository = processedEventRepository;
        this.impactWorker = impactWorker;
    }

    @KafkaListener(topics = "impact-analysis-events", groupId = CONSUMER_GROUP)
    public void onMessage(String message) {
        JsonNode root;
        try {
            root = objectMapper.readTree(message);
        } catch (Exception e) {
            // Malformed JSON will never parse no matter how many times it's
            // retried - route it straight to the DLQ instead of dropping it.
            throw new NonRetryableEventException("Malformed impact-analysis-events message: " + message, e);
        }

        String eventType = root.path("eventType").asText(null);
        if (!"IMPACT_ANALYSIS_REQUESTED".equals(eventType)) {
            // Not an error - this topic also carries this service's own
            // COMPLETED/FAILED events (see class comment), so most messages seen
            // here are expected to be ignored, not failures.
            log.debug("Ignoring {} event on impact-analysis-events (not this consumer's concern)", eventType);
            return;
        }

        EventEnvelope<ImpactAnalysisRequestedPayload> envelope;
        try {
            envelope = objectMapper.readValue(message, new TypeReference<>() {
            });
        } catch (Exception e) {
            // The envelope declared itself as IMPACT_ANALYSIS_REQUESTED but its
            // payload doesn't match that shape - a genuinely corrupt/poison
            // message, not something retrying will fix.
            throw new NonRetryableEventException("Malformed IMPACT_ANALYSIS_REQUESTED payload: " + message, e);
        }

        if (processedEventRepository.existsById(envelope.eventId())) {
            log.debug("Event {} already processed, skipping (idempotent replay)", envelope.eventId());
            return;
        }

        impactWorker.process(envelope.payload().analysisId());
        markProcessed(envelope);
    }

    private void markProcessed(EventEnvelope<ImpactAnalysisRequestedPayload> envelope) {
        ProcessedEvent processedEvent = new ProcessedEvent();
        processedEvent.setEventId(envelope.eventId());
        processedEvent.setEventType(envelope.eventType().name());
        processedEvent.setConsumerGroup(CONSUMER_GROUP);
        processedEvent.setProcessedAt(Instant.now());
        processedEventRepository.save(processedEvent);
    }
}
