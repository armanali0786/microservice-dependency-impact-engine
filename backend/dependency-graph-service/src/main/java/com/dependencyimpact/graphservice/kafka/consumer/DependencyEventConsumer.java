package com.dependencyimpact.graphservice.kafka.consumer;

import com.dependencyimpact.common.events.EventEnvelope;
import com.dependencyimpact.common.exceptions.NonRetryableEventException;
import com.dependencyimpact.common.model.DependencyObservation;
import com.dependencyimpact.graphservice.entity.Dependency;
import com.dependencyimpact.graphservice.entity.ProcessedEvent;
import com.dependencyimpact.graphservice.entity.ServiceLookup;
import com.dependencyimpact.graphservice.repository.DependencyRepository;
import com.dependencyimpact.graphservice.repository.ProcessedEventRepository;
import com.dependencyimpact.graphservice.repository.ServiceLookupRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Component
public class DependencyEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(DependencyEventConsumer.class);
    private static final String CONSUMER_GROUP = "dependency-graph-consumers";

    private final ObjectMapper objectMapper;
    private final ProcessedEventRepository processedEventRepository;
    private final ServiceLookupRepository serviceLookupRepository;
    private final DependencyRepository dependencyRepository;

    public DependencyEventConsumer(ObjectMapper objectMapper,
                                    ProcessedEventRepository processedEventRepository,
                                    ServiceLookupRepository serviceLookupRepository,
                                    DependencyRepository dependencyRepository) {
        this.objectMapper = objectMapper;
        this.processedEventRepository = processedEventRepository;
        this.serviceLookupRepository = serviceLookupRepository;
        this.dependencyRepository = dependencyRepository;
    }

    @KafkaListener(topics = "dependency-events", groupId = CONSUMER_GROUP)
    public void onMessage(String message) {
        EventEnvelope<DependencyObservation> envelope;
        try {
            envelope = objectMapper.readValue(message, new TypeReference<>() {
            });
        } catch (Exception e) {
            // Malformed JSON will never parse no matter how many times it's retried -
            // let the container's error handler route it straight to the DLQ
            // (dependency-events-dlq) instead of silently dropping it.
            throw new NonRetryableEventException("Malformed dependency-events message: " + message, e);
        }

        if (processedEventRepository.existsById(envelope.eventId())) {
            log.debug("Event {} already processed, skipping (idempotent replay)", envelope.eventId());
            return;
        }

        DependencyObservation observation = envelope.payload();
        Optional<ServiceLookup> source = serviceLookupRepository.findByName(observation.sourceServiceId());
        Optional<ServiceLookup> target = serviceLookupRepository.findByName(observation.targetServiceId());

        if (source.isEmpty() || target.isEmpty()) {
            // Deliberately not an exception: this is a legitimate "not registered
            // yet" business state, not a malformed message, so it shouldn't burn
            // retries or land in the DLQ. Known limitation carried over from
            // Milestone 2: if the service never gets registered, this event is
            // dropped permanently rather than retried once it might resolve.
            log.warn("Unknown service(s) for event {}: {} -> {}. Skipping until both are registered.",
                    envelope.eventId(), observation.sourceServiceId(), observation.targetServiceId());
            return;
        }

        upsertDependency(source.get().getId(), target.get().getId(), observation);
        markProcessed(envelope);
    }

    private void upsertDependency(UUID sourceId, UUID targetId, DependencyObservation observation) {
        Dependency dependency = new Dependency();
        dependency.setId(UUID.randomUUID());
        dependency.setSourceServiceId(sourceId);
        dependency.setTargetServiceId(targetId);
        dependency.setDependencyType(observation.type().name());
        dependency.setProtocol(observation.protocol());
        dependency.setEndpoint(observation.endpoint());
        dependency.setTopicName(observation.topic());
        dependency.setEnvironment(observation.environment());
        dependency.setConfidence(observation.confidence() != null ? observation.confidence().name() : null);
        dependency.setStatus("ACTIVE");

        Instant now = Instant.now();
        dependency.setFirstSeenAt(now);
        dependency.setLastSeenAt(now);
        dependency.setCreatedAt(now);
        dependency.setUpdatedAt(now);

        dependencyRepository.save(dependency);
    }

    private void markProcessed(EventEnvelope<DependencyObservation> envelope) {
        ProcessedEvent processedEvent = new ProcessedEvent();
        processedEvent.setEventId(envelope.eventId());
        processedEvent.setEventType(envelope.eventType().name());
        processedEvent.setConsumerGroup(CONSUMER_GROUP);
        processedEvent.setProcessedAt(Instant.now());
        processedEventRepository.save(processedEvent);
    }
}
