CREATE TABLE users (
    id UUID PRIMARY KEY,

    email VARCHAR(255) NOT NULL,
    full_name VARCHAR(255),
    password_hash VARCHAR(255) NOT NULL,

    role VARCHAR(30) NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',

    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,

    CONSTRAINT uk_users_email UNIQUE (email)
);
