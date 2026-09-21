CREATE TABLE external_dependencies (
    id UUID PRIMARY KEY,

    service_id UUID NOT NULL,

    name VARCHAR(200) NOT NULL,
    dependency_type VARCHAR(50) NOT NULL,
    endpoint TEXT,

    environment VARCHAR(50) NOT NULL,

    criticality VARCHAR(30),
    failure_behavior VARCHAR(30),

    first_seen_at TIMESTAMPTZ,
    last_seen_at TIMESTAMPTZ,

    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',

    metadata JSONB,

    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,

    CONSTRAINT fk_external_dependency_service
        FOREIGN KEY (service_id)
        REFERENCES services(id)
);
