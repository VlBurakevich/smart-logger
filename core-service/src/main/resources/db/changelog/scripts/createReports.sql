CREATE TABLE reports
(
    id                 UUID PRIMARY KEY,
    monitoring_task_id UUID REFERENCES monitoring_tasks (id),
    totalSnapshots     INTEGER,
    averageAiScore     DOUBLE PRECISION,
    maxAiScore         DOUBLE PRECISION,
    topRootCauses      JSONB,
    created_at         TIMESTAMPTZ NOT NULL
);