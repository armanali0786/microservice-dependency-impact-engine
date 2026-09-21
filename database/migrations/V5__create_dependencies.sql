CREATE TABLE dependencies (
    id UUID PRIMARY KEY,

    source_service_id UUID NOT NULL,
    target_service_id UUID NOT NULL,

    dependency_type VARCHAR(50) NOT NULL,
    protocol VARCHAR(50),

    api_id UUID,
    endpoint VARCHAR(500),
    topic_name VARCHAR(255),

    environment VARCHAR(50) NOT NULL,

    criticality VARCHAR(30),
    failure_behavior VARCHAR(30),
    confidence VARCHAR(30),

    latency_ms DOUBLE PRECISION,
    error_rate DOUBLE PRECISION,

    first_seen_at TIMESTAMPTZ,
    last_seen_at TIMESTAMPTZ,

    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',

    metadata JSONB,

    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,

    CONSTRAINT fk_dependency_source
        FOREIGN KEY (source_service_id)
        REFERENCES services(id),

    CONSTRAINT fk_dependency_target
        FOREIGN KEY (target_service_id)
        REFERENCES services(id),

    CONSTRAINT fk_dependency_api
        FOREIGN KEY (api_id)
        REFERENCES apis(id)
);

CREATE INDEX idx_dependencies_source ON dependencies(source_service_id);
CREATE INDEX idx_dependencies_target ON dependencies(target_service_id);
CREATE INDEX idx_dependencies_environment ON dependencies(environment);
CREATE INDEX idx_dependencies_type ON dependencies(dependency_type);
CREATE INDEX idx_dependencies_last_seen ON dependencies(last_seen_at);
CREATE INDEX idx_dependencies_source_environment ON dependencies(source_service_id, environment);
CREATE INDEX idx_dependencies_target_environment ON dependencies(target_service_id, environment);
