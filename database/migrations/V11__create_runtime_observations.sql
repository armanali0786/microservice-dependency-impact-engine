CREATE TABLE runtime_observations (
    id UUID PRIMARY KEY,

    trace_id VARCHAR(255),
    span_id VARCHAR(255),

    source_service_id UUID,
    target_service_id UUID,

    endpoint VARCHAR(500),
    http_method VARCHAR(20),

    status_code INTEGER,

    latency_ms DOUBLE PRECISION,

    observed_at TIMESTAMPTZ NOT NULL,

    environment VARCHAR(50) NOT NULL,

    metadata JSONB,

    CONSTRAINT fk_runtime_source
        FOREIGN KEY (source_service_id)
        REFERENCES services(id),

    CONSTRAINT fk_runtime_target
        FOREIGN KEY (target_service_id)
        REFERENCES services(id)
);

CREATE INDEX idx_runtime_source ON runtime_observations(source_service_id);
CREATE INDEX idx_runtime_target ON runtime_observations(target_service_id);
CREATE INDEX idx_runtime_observed_at ON runtime_observations(observed_at);
CREATE INDEX idx_runtime_environment ON runtime_observations(environment);
CREATE INDEX idx_runtime_trace ON runtime_observations(trace_id);
