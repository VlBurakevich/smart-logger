CREATE TABLE reports
(
    id            uuid PRIMARY KEY,
    account_id    uuid REFERENCES log_monitoring_settings (account_id),
    content       jsonb,
    error_count   INTEGER,
    anomaly_count INTEGER,
    summary       TEXT,
    created_at    TIMESTAMPTZ
);