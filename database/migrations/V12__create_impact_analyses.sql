CREATE TABLE impact_analyses (
    id UUID PRIMARY KEY,

    source_service_id UUID NOT NULL,

    change_type VARCHAR(50) NOT NULL,

    environment VARCHAR(50) NOT NULL,

    api_id UUID,

    topic_id UUID,

    traversal_depth INTEGER NOT NULL,

    status VARCHAR(30) NOT NULL,

    risk_level VARCHAR(30),

    started_at TIMESTAMPTZ,
    completed_at TIMESTAMPTZ,

    error_message TEXT,

    created_by UUID,

    created_at TIMESTAMPTZ NOT NULL,

    CONSTRAINT fk_impact_service
        FOREIGN KEY (source_service_id)
        REFERENCES services(id),

    CONSTRAINT fk_impact_api
        FOREIGN KEY (api_id)
        REFERENCES apis(id),

    CONSTRAINT fk_impact_topic
        FOREIGN KEY (topic_id)
        REFERENCES kafka_topics(id)
);
