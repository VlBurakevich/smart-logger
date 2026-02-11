CREATE TYPE snapshot_status AS ENUM ('PENDING', 'COMPLETED', 'FAILED');

CREATE TABLE snapshots
(
    id                 UUID PRIMARY KEY,
    monitoring_task_id UUID REFERENCES monitoring_tasks (id),
    snapshot_time      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    status             snapshot_status      DEFAULT 'PENDING' NOT NULL,
    errors             JSONB,
    max_severity       DOUBLE PRECISION     DEFAULT 0.00,
    anomalies          JSONB,
    ai_description     TEXT,
    ai_score           DOUBLE PRECISION     DEFAULT 0.00,
    created_at         TIMESTAMPTZ          DEFAULT NOW()
);

CREATE INDEX idx_snapshots_account_time ON snapshots (monitoring_task_id, created_at DESC);
CREATE INDEX idx_snapshots_errors_gin ON snapshots USING GIN (errors);
CREATE INDEX idx_snapshots_anomalies_gin ON snapshots USING GIN (anomalies);