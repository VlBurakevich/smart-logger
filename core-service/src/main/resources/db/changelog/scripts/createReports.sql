CREATE TYPE report_status AS ENUM ('PENDING', 'AWAITING_REPLY', 'COMPLETED', 'FAILED');

CREATE TABLE reports
(
    id                 UUID PRIMARY KEY,
    monitoring_task_id UUID REFERENCES monitoring_tasks (id),
    report_start_time  TIMESTAMPTZ,
    report_end_time    TIMESTAMPTZ,
    status             report_status DEFAULT 'PENDING' NOT NULL,
    summary            TEXT,
    log_level_count    JSONB,
    updated_at         TIMESTAMPTZ,
    created_at         TIMESTAMPTZ                     NOT NULL
);