CREATE TABLE kafka_topics (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    environment VARCHAR(50) NOT NULL,
    description TEXT,
    schema_version VARCHAR(100),
    partition_count INTEGER,
    retention_ms BIGINT,
    status VARCHAR(30) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,

    CONSTRAINT uk_kafka_topic_environment
        UNIQUE (name, environment)
);
