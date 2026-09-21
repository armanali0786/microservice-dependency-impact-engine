CREATE TABLE service_environments (
    id UUID PRIMARY KEY,
    service_id UUID NOT NULL,
    environment VARCHAR(50) NOT NULL,
    deployment_version VARCHAR(100),
    status VARCHAR(30) NOT NULL,
    endpoint_url TEXT,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,

    CONSTRAINT fk_service_environment_service
        FOREIGN KEY (service_id)
        REFERENCES services(id),

    CONSTRAINT uk_service_environment
        UNIQUE (service_id, environment)
);
