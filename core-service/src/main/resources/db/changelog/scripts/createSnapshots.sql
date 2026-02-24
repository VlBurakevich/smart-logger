CREATE TYPE snapshot_status AS ENUM ('PENDING', 'AWAITING_REPLY', 'COMPLETED', 'FAILED');

CREATE TABLE snapshots
(
    id                  UUID PRIMARY KEY,
    monitoring_task_id  UUID REFERENCES monitoring_tasks (id),
    snapshot_start_time TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    snapshot_end_time   TIMESTAMPTZ,
    status              snapshot_status      DEFAULT 'PENDING' NOT NULL,
    errors              JSONB,
    max_severity        DOUBLE PRECISION     DEFAULT 0.00,
    root_case           TEXT,
    suggested_action    TEXT,
    ai_description      TEXT,
    ai_score            DOUBLE PRECISION     DEFAULT 0.00,
    created_at          TIMESTAMPTZ          DEFAULT NOW()
);