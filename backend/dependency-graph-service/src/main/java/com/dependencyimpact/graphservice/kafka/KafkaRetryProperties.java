package com.dependencyimpact.graphservice.kafka;

import org.springframework.boot.context.properties.ConfigurationProperties;

// Values match docs/kafka-spec.md section 32's exact backoff sequence: 1s, 2s, 4s,
// 8s (4 retries after the first attempt, doubling each time) before giving up and
// routing to the DLQ.
@ConfigurationProperties(prefix = "kafka.retry")
public class KafkaRetryProperties {

    private int maxRetries = 4;
    private long initialIntervalMs = 1000;
    private double multiplier = 2.0;

    public int getMaxRetries() {
        return maxRetries;
    }

    public void setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    public long getInitialIntervalMs() {
        return initialIntervalMs;
    }

    public void setInitialIntervalMs(long initialIntervalMs) {
        this.initialIntervalMs = initialIntervalMs;
    }

    public double getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(double multiplier) {
        this.multiplier = multiplier;
    }
}
