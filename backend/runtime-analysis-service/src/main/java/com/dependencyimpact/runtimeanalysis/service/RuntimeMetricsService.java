package com.dependencyimpact.runtimeanalysis.service;

import com.dependencyimpact.runtimeanalysis.dto.RuntimeMetricsResponse;
import com.dependencyimpact.runtimeanalysis.entity.RuntimeObservation;
import com.dependencyimpact.runtimeanalysis.repository.RuntimeObservationRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

// Latency aggregation and error-rate aggregation folded into one class: both are
// just different reductions over the same observation window, so splitting them
// into separate services would only mean querying the same rows twice.
@Service
public class RuntimeMetricsService {

    private final RuntimeObservationRepository runtimeObservationRepository;

    public RuntimeMetricsService(RuntimeObservationRepository runtimeObservationRepository) {
        this.runtimeObservationRepository = runtimeObservationRepository;
    }

    public RuntimeMetricsResponse computeMetrics(UUID serviceId, String environment, Instant from, Instant to) {
        Instant resolvedFrom = from != null ? from : Instant.now().minus(1, ChronoUnit.HOURS);
        Instant resolvedTo = to != null ? to : Instant.now();

        List<RuntimeObservation> observations = runtimeObservationRepository
                .findByTargetServiceIdAndEnvironmentAndObservedAtBetween(serviceId, environment, resolvedFrom, resolvedTo);

        if (observations.isEmpty()) {
            return new RuntimeMetricsResponse(0, 0, 0, 0, 0);
        }

        long windowSeconds = Math.max(1, Duration.between(resolvedFrom, resolvedTo).getSeconds());
        double requestRate = (double) observations.size() / windowSeconds;

        long errorCount = observations.stream()
                .filter(o -> o.getStatusCode() != null && o.getStatusCode() >= 500)
                .count();
        double errorRate = (double) errorCount / observations.size();

        List<Double> sortedLatencies = observations.stream()
                .map(RuntimeObservation::getLatencyMs)
                .filter(latency -> latency != null)
                .sorted(Comparator.naturalOrder())
                .toList();

        double p95 = percentile(sortedLatencies, 0.95);
        double p99 = percentile(sortedLatencies, 0.99);

        return new RuntimeMetricsResponse(requestRate, errorRate, p95, p99, observations.size());
    }

    // Nearest-rank percentile - simple and explainable, not interpolated. Good
    // enough for this MVP's sample sizes; a proper histogram/t-digest approach
    // would matter at much higher volume than a learning project generates.
    private double percentile(List<Double> sortedValues, double percentile) {
        if (sortedValues.isEmpty()) {
            return 0;
        }
        int index = (int) Math.ceil(percentile * sortedValues.size()) - 1;
        index = Math.max(0, Math.min(index, sortedValues.size() - 1));
        return sortedValues.get(index);
    }
}
