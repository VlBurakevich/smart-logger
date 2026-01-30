CREATE TABLE reports
(
    id                     uuid PRIMARY KEY,
    service_config_id uuid REFERENCES service_configs (id),
    content                jsonb,
    error_count            INTEGER,
    anomaly_count          INTEGER,
    summary                TEXT,
    created_at             TIMESTAMPTZ NOT NULL
);