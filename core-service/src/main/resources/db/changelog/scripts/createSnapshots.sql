CREATE TYPE snapshot_status AS ENUM ('PENDING', 'AWAITING_REPLY', 'COMPLETED', 'FAILED');

CREATE TABLE snapshots
(
    id                 UUID PRIMARY KEY,
    monitoring_task_id UUID REFERENCES monitoring_tasks (id),
    snapshot_time      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    status             snapshot_status      DEFAULT 'PENDING' NOT NULL,
    errors             JSONB,
    max_severity       DOUBLE PRECISION     DEFAULT 0.00,
    rootCase           TEXT,
    suggestedAction    TEXT,
    ai_description     TEXT,
    ai_score           DOUBLE PRECISION     DEFAULT 0.00,
    created_at         TIMESTAMPTZ          DEFAULT NOW()
);