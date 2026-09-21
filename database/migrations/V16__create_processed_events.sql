CREATE TABLE processed_events (
    event_id VARCHAR(255) PRIMARY KEY,

    event_type VARCHAR(100) NOT NULL,

    consumer_group VARCHAR(255) NOT NULL,

    processed_at TIMESTAMPTZ NOT NULL,

    metadata JSONB
);
