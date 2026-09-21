CREATE TABLE kafka_service_bindings (
    id UUID PRIMARY KEY,

    service_id UUID NOT NULL,
    topic_id UUID NOT NULL,

    binding_type VARCHAR(30) NOT NULL,

    consumer_group VARCHAR(255),
    schema_version VARCHAR(100),

    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,

    CONSTRAINT fk_kafka_binding_service
        FOREIGN KEY (service_id)
        REFERENCES services(id),

    CONSTRAINT fk_kafka_binding_topic
        FOREIGN KEY (topic_id)
        REFERENCES kafka_topics(id)
);

CREATE INDEX idx_kafka_binding_service ON kafka_service_bindings(service_id);
CREATE INDEX idx_kafka_binding_topic ON kafka_service_bindings(topic_id);
CREATE INDEX idx_kafka_binding_type ON kafka_service_bindings(binding_type);
