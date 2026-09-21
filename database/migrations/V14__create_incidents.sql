CREATE TABLE incidents (
    id UUID PRIMARY KEY,

    service_id UUID NOT NULL,

    title VARCHAR(255) NOT NULL,
    description TEXT,

    severity VARCHAR(30) NOT NULL,
    status VARCHAR(30) NOT NULL,

    error_rate DOUBLE PRECISION,
    latency_ms DOUBLE PRECISION,
    kafka_lag BIGINT,

    started_at TIMESTAMPTZ NOT NULL,
    resolved_at TIMESTAMPTZ,

    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,

    CONSTRAINT fk_incident_service
        FOREIGN KEY (service_id)
        REFERENCES services(id)
);
