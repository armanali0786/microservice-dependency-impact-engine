package com.dependencyimpact.graphservice.entity;

@jakarta.persistence.Entity
@jakarta.persistence.Table(name = "processed_events")
public class ProcessedEvent {
    @jakarta.persistence.Id
    @jakarta.persistence.Column(name = "event_id")
    private String eventId;

    @jakarta.persistence.Column(name = "event_type")
    private String eventType;

    @jakarta.persistence.Column(name = "consumer_group")
    private String consumerGroup;

    @jakarta.persistence.Column(name = "processed_at")
    private java.time.Instant processedAt;

    @jakarta.persistence.Column(name = "metadata")
    private String metadata;
}
