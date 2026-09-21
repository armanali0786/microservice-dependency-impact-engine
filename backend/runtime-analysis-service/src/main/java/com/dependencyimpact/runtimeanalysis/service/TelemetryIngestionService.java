package com.dependencyimpact.runtimeanalysis.service;

import com.dependencyimpact.common.exceptions.ResourceNotFoundException;
import com.dependencyimpact.runtimeanalysis.dto.RuntimeObservationReceivedPayload;
import com.dependencyimpact.runtimeanalysis.dto.RuntimeObservationRequest;
import com.dependencyimpact.runtimeanalysis.dto.RuntimeObservationResponse;
import com.dependencyimpact.runtimeanalysis.entity.RuntimeObservation;
import com.dependencyimpact.runtimeanalysis.kafka.producer.RuntimeEventProducer;
import com.dependencyimpact.runtimeanalysis.mapper.RuntimeObservationMapper;
import com.dependencyimpact.runtimeanalysis.repository.RuntimeObservationRepository;
import com.dependencyimpact.runtimeanalysis.repository.ServiceLookupRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

// This is the "Telemetry ingestion" + "Trace processing" responsibility from
// docs/technical-implementation.md section 28 combined into one class - there's no
// separate trace-parsing step here because observations arrive already-shaped via
// REST (see OtelExportConsumer for why the Kafka/OTel-collector path is deferred).
@Service
public class TelemetryIngestionService {

    private final RuntimeObservationRepository runtimeObservationRepository;
    private final ServiceLookupRepository serviceLookupRepository;
    private final RuntimeObservationMapper mapper;
    private final RuntimeEventProducer runtimeEventProducer;

    public TelemetryIngestionService(RuntimeObservationRepository runtimeObservationRepository,
                                      ServiceLookupRepository serviceLookupRepository,
                                      RuntimeObservationMapper mapper,
                                      RuntimeEventProducer runtimeEventProducer) {
        this.runtimeObservationRepository = runtimeObservationRepository;
        this.serviceLookupRepository = serviceLookupRepository;
        this.mapper = mapper;
        this.runtimeEventProducer = runtimeEventProducer;
    }

    public RuntimeObservationResponse ingest(RuntimeObservationRequest request) {
        // runtime_observations has FKs on both source/target service - the same
        // "FK violation leaking out as a raw 500" bug already fixed once in
        // impact-analysis-service, so check up front here too.
        requireServiceExists(request.getSourceServiceId());
        requireServiceExists(request.getTargetServiceId());

        RuntimeObservation observation = mapper.toEntity(request);
        runtimeObservationRepository.save(observation);

        runtimeEventProducer.publishObservationReceived(new RuntimeObservationReceivedPayload(
                observation.getSourceServiceId(),
                observation.getTargetServiceId(),
                observation.getLatencyMs(),
                observation.getStatusCode(),
                observation.getEnvironment()
        ));

        return mapper.toResponse(observation);
    }

    public List<RuntimeObservationResponse> listForService(UUID serviceId, String environment,
                                                             Instant from, Instant to,
                                                             Integer statusCode, String endpoint) {
        Instant resolvedFrom = from != null ? from : Instant.now().minus(1, ChronoUnit.HOURS);
        Instant resolvedTo = to != null ? to : Instant.now();

        return runtimeObservationRepository
                .findByTargetServiceIdAndEnvironmentAndObservedAtBetween(serviceId, environment, resolvedFrom, resolvedTo)
                .stream()
                .filter(o -> statusCode == null || statusCode.equals(o.getStatusCode()))
                .filter(o -> endpoint == null || endpoint.equals(o.getEndpoint()))
                .map(mapper::toResponse)
                .toList();
    }

    private void requireServiceExists(UUID serviceId) {
        if (!serviceLookupRepository.existsById(serviceId)) {
            throw new ResourceNotFoundException("Service not found: " + serviceId);
        }
    }
}
