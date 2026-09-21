package com.dependencyimpact.runtimeanalysis.dto;

public class RuntimeMetricsResponse {

    private final double requestRate;
    private final double errorRate;
    private final double p95LatencyMs;
    private final double p99LatencyMs;
    private final int sampleCount;

    public RuntimeMetricsResponse(double requestRate, double errorRate, double p95LatencyMs,
                                   double p99LatencyMs, int sampleCount) {
        this.requestRate = requestRate;
        this.errorRate = errorRate;
        this.p95LatencyMs = p95LatencyMs;
        this.p99LatencyMs = p99LatencyMs;
        this.sampleCount = sampleCount;
    }

    public double getRequestRate() {
        return requestRate;
    }

    public double getErrorRate() {
        return errorRate;
    }

    public double getP95LatencyMs() {
        return p95LatencyMs;
    }

    public double getP99LatencyMs() {
        return p99LatencyMs;
    }

    public int getSampleCount() {
        return sampleCount;
    }
}
