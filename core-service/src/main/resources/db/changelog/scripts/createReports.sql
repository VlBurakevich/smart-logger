CREATE TABLE reports
(
    id                     uuid PRIMARY KEY,
    monitoring_settings_id uuid REFERENCES monitoring_settings (id),
    content                jsonb,
    error_count            INTEGER,
    anomaly_count          INTEGER,
    summary                TEXT,
    created_at             TIMESTAMPTZ NOT NULL
);