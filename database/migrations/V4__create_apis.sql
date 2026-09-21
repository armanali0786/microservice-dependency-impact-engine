CREATE TABLE apis (
    id UUID PRIMARY KEY,
    service_id UUID NOT NULL,
    name VARCHAR(200) NOT NULL,
    path VARCHAR(500) NOT NULL,
    http_method VARCHAR(20) NOT NULL,
    version VARCHAR(50),
    request_schema JSONB,
    response_schema JSONB,
    deprecated BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,

    CONSTRAINT fk_apis_service
        FOREIGN KEY (service_id)
        REFERENCES services(id)
);

CREATE UNIQUE INDEX uk_api_definition
ON apis(service_id, http_method, path, version);
