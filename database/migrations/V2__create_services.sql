CREATE TABLE services (
    id UUID PRIMARY KEY,
    team_id UUID NOT NULL,
    name VARCHAR(150) NOT NULL,
    description TEXT,
    technology VARCHAR(100),
    repository_url TEXT,
    repository_name VARCHAR(255),
    status VARCHAR(30) NOT NULL,
    version VARCHAR(100),
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,

    CONSTRAINT fk_services_team
        FOREIGN KEY (team_id)
        REFERENCES teams(id),

    CONSTRAINT uk_services_name
        UNIQUE (name)
);
