CREATE TABLE service_database_dependencies (
    id UUID PRIMARY KEY,

    service_id UUID NOT NULL,
    database_id UUID NOT NULL,

    access_type VARCHAR(30) NOT NULL,

    tables_used JSONB,

    first_seen_at TIMESTAMPTZ,
    last_seen_at TIMESTAMPTZ,

    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',

    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,

    CONSTRAINT fk_service_db_service
        FOREIGN KEY (service_id)
        REFERENCES services(id),

    CONSTRAINT fk_service_db_database
        FOREIGN KEY (database_id)
        REFERENCES databases(id)
);
