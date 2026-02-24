CREATE TABLE reports
(
    id                 UUID PRIMARY KEY,
    monitoring_task_id UUID REFERENCES monitoring_tasks (id),
    content            JSONB,
    error_count        INTEGER,
    anomaly_count      INTEGER,
    summary            TEXT,
    created_at         TIMESTAMPTZ NOT NULL
);