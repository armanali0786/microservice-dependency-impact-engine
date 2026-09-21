CREATE TABLE audit_logs (
    id UUID PRIMARY KEY,

    user_id UUID,

    action VARCHAR(100) NOT NULL,

    resource_type VARCHAR(100),
    resource_id UUID,

    correlation_id VARCHAR(255),

    metadata JSONB,

    created_at TIMESTAMPTZ NOT NULL
);

CREATE INDEX idx_audit_user ON audit_logs(user_id);
CREATE INDEX idx_audit_resource ON audit_logs(resource_type, resource_id);
CREATE INDEX idx_audit_created_at ON audit_logs(created_at);
CREATE INDEX idx_audit_correlation ON audit_logs(correlation_id);
