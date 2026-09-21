CREATE TABLE impact_components (
    id UUID PRIMARY KEY,

    impact_analysis_id UUID NOT NULL,

    service_id UUID,

    dependency_type VARCHAR(50),

    depth INTEGER NOT NULL,

    impact_level VARCHAR(30),

    reason TEXT,

    dependency_path JSONB,

    runtime_evidence BOOLEAN NOT NULL DEFAULT FALSE,

    critical_dependency BOOLEAN NOT NULL DEFAULT FALSE,

    created_at TIMESTAMPTZ NOT NULL,

    CONSTRAINT fk_impact_component_analysis
        FOREIGN KEY (impact_analysis_id)
        REFERENCES impact_analyses(id),

    CONSTRAINT fk_impact_component_service
        FOREIGN KEY (service_id)
        REFERENCES services(id)
);

CREATE INDEX idx_impact_components_analysis ON impact_components(impact_analysis_id);
CREATE INDEX idx_impact_components_service ON impact_components(service_id);
CREATE INDEX idx_impact_components_level ON impact_components(impact_level);
