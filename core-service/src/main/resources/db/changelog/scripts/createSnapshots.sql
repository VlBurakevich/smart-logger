CREATE TABLE snapshots
(
    id                    UUID PRIMARY KEY,
    monitoring_setting_id UUID REFERENCES monitoring_settings (id),
    snapshot_time         TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    errors                JSONB,
    max_severity          DECIMAL(5, 2)        DEFAULT 0.00,
    anomalies             JSONB,
    ai_description        TEXT,
    ai_score              DECIMAL(5, 2)        DEFAULT 0.00,
    raw_snippet           TEXT,
    created_at            TIMESTAMPTZ          DEFAULT NOW()
);

CREATE INDEX idx_snapshots_account_time ON snapshots (monitoring_setting_id, created_at DESC);
CREATE INDEX idx_snapshots_errors_gin ON snapshots USING GIN (errors);
CREATE INDEX idx_snapshots_anomalies_gin ON snapshots USING GIN (anomalies);