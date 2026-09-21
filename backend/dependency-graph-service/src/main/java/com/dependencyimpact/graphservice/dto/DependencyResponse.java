package com.dependencyimpact.graphservice.dto;

import java.time.Instant;
import java.util.UUID;

public class DependencyResponse {

    private final UUID id;
    private final UUID sourceServiceId;
    private final UUID targetServiceId;
    private final String dependencyType;
    private final String protocol;
    private final String endpoint;
    private final String topicName;
    private final String environment;
    private final String confidence;
    private final String status;
    private final Instant firstSeenAt;
    private final Instant lastSeenAt;

    public DependencyResponse(UUID id, UUID sourceServiceId, UUID targetServiceId, String dependencyType,
                               String protocol, String endpoint, String topicName, String environment,
                               String confidence, String status, Instant firstSeenAt, Instant lastSeenAt) {
        this.id = id;
        this.sourceServiceId = sourceServiceId;
        this.targetServiceId = targetServiceId;
        this.dependencyType = dependencyType;
        this.protocol = protocol;
        this.endpoint = endpoint;
        this.topicName = topicName;
        this.environment = environment;
        this.confidence = confidence;
        this.status = status;
        this.firstSeenAt = firstSeenAt;
        this.lastSeenAt = lastSeenAt;
    }

    public UUID getId() {
        return id;
    }

    public UUID getSourceServiceId() {
        return sourceServiceId;
    }

    public UUID getTargetServiceId() {
        return targetServiceId;
    }

    public String getDependencyType() {
        return dependencyType;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public String getTopicName() {
        return topicName;
    }

    public String getEnvironment() {
        return environment;
    }

    public String getConfidence() {
        return confidence;
    }

    public String getStatus() {
        return status;
    }

    public Instant getFirstSeenAt() {
        return firstSeenAt;
    }

    public Instant getLastSeenAt() {
        return lastSeenAt;
    }
}
