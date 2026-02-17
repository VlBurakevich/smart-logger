CREATE TABLE monitoring_tasks
(
    id                  UUID PRIMARY KEY,
    api_key_id          UUID         NOT NULL REFERENCES api_keys (id) ON DELETE CASCADE,
    service_name        VARCHAR(100) NOT NULL,
    snapshot_sec        INTEGER,
    report_hr           INTEGER,
    current_snapshot_id UUID,
    last_snapshot_at    TIMESTAMPTZ,
    current_report_id   UUID,
    last_report_at      TIMESTAMPTZ,
    created_at          TIMESTAMPTZ,
    updated_at          TIMESTAMPTZ,
    version             BIGINT,

    CONSTRAINT uk_key_service_name UNIQUE (api_key_id, service_name)
);