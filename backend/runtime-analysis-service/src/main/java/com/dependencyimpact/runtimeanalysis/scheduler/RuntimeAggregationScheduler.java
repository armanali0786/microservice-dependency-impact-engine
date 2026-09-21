package com.dependencyimpact.runtimeanalysis.scheduler;

import com.dependencyimpact.runtimeanalysis.dto.RuntimeMetricsResponse;
import com.dependencyimpact.runtimeanalysis.repository.RuntimeObservationRepository;
import com.dependencyimpact.runtimeanalysis.repository.RuntimeObservationRepository.ServiceEnvironmentPair;
import com.dependencyimpact.runtimeanalysis.service.IncidentDetectionService;
import com.dependencyimpact.runtimeanalysis.service.RuntimeMetricsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

// This is what turns "Incident detection" from a manual-only API into something
// that actually watches the fleet: every tick, re-aggregate metrics for every
// (service, environment) pair that's had traffic recently, and let
// IncidentDetectionService decide whether that crosses a threshold.
@Component
public class RuntimeAggregationScheduler {

    private static final Logger log = LoggerFactory.getLogger(RuntimeAggregationScheduler.class);
    private static final int LOOKBACK_MINUTES = 15;

    private final RuntimeObservationRepository runtimeObservationRepository;
    private final RuntimeMetricsService runtimeMetricsService;
    private final IncidentDetectionService incidentDetectionService;

    public RuntimeAggregationScheduler(RuntimeObservationRepository runtimeObservationRepository,
                                        RuntimeMetricsService runtimeMetricsService,
                                        IncidentDetectionService incidentDetectionService) {
        this.runtimeObservationRepository = runtimeObservationRepository;
        this.runtimeMetricsService = runtimeMetricsService;
        this.incidentDetectionService = incidentDetectionService;
    }

    @Scheduled(fixedDelay = 60000, initialDelay = 15000)
    public void evaluateActiveServices() {
        Instant since = Instant.now().minus(LOOKBACK_MINUTES, ChronoUnit.MINUTES);
        Instant now = Instant.now();

        for (ServiceEnvironmentPair pair : runtimeObservationRepository.findDistinctTargetServiceEnvironmentPairsSince(since)) {
            RuntimeMetricsResponse metrics = runtimeMetricsService.computeMetrics(
                    pair.getTargetServiceId(), pair.getEnvironment(), since, now);

            incidentDetectionService.evaluate(pair.getTargetServiceId(), metrics)
                    .ifPresent(incident -> log.info("Auto-detected incident {} for service {} ({})",
                            incident.getId(), pair.getTargetServiceId(), incident.getDescription()));
        }
    }
}
