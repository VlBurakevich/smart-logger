CREATE TYPE snapshot_status AS ENUM ('PENDING', 'AWAITING_REPLY', 'COMPLETED', 'FAILED');

CREATE TABLE snapshots
(
    id                  UUID PRIMARY KEY,
    monitoring_task_id  UUID REFERENCES monitoring_tasks (id),
    snapshot_start_time TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    snapshot_end_time   TIMESTAMPTZ NOT NULL,
    status              snapshot_status      DEFAULT 'PENDING' NOT NULL,
    errors              JSONB,
    max_severity        DOUBLE PRECISION     DEFAULT 0.00,
    root_cause          TEXT,
    suggested_action    TEXT,
    ai_description      TEXT,
    ai_score            DOUBLE PRECISION     DEFAULT 0.00,
    updated_at          TIMESTAMPTZ          DEFAULT NOW(),
    created_at          TIMESTAMPTZ          DEFAULT NOW()
);