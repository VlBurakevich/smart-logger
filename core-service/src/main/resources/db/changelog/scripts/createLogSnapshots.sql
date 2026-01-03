CREATE TABLE log_snapshots
(
    id             UUID PRIMARY KEY,
    account_id     UUID REFERENCES log_monitoring_settings (account_id),
    timestamp      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    errors         JSONB,
    max_severity   DECIMAL(5, 2)        DEFAULT 0.00,
    anomalies      JSONB,
    ai_description TEXT,
    ai_score       DECIMAL(5, 2)        DEFAULT 0.00,
    raw_snippet    TEXT,
    created_at     TIMESTAMPTZ          DEFAULT NOW()
);

CREATE INDEX idx_log_snapshots_account_snapshot ON log_snapshots (account_id, created_at DESC);
CREATE INDEX idx_log_snapshots_errors_gin ON log_snapshots USING GIN (errors);
CREATE INDEX idx_log_snapshots_anomalies_gin ON log_snapshots USING GIN (anomalies);