CREATE TABLE databases (
    id UUID PRIMARY KEY,

    name VARCHAR(200) NOT NULL,
    database_type VARCHAR(100) NOT NULL,
    host_identifier VARCHAR(255),
    environment VARCHAR(50) NOT NULL,

    status VARCHAR(30) NOT NULL,

    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,

    CONSTRAINT uk_database_environment
        UNIQUE (name, environment)
);
