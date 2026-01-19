CREATE TABLE monitoring_settings
(
    id              UUID PRIMARY KEY,
    api_key_id      UUID         NOT NULL REFERENCES api_keys (id) ON DELETE CASCADE,
    service_name    VARCHAR(100) NOT NULL,
    is_active       BOOLEAN      NOT NULL DEFAULT TRUE,
    check_min       INTEGER,
    report_hr       INTEGER,
    last_checked_at TIMESTAMPTZ,
    last_report_at  TIMESTAMPTZ,
    created_at      TIMESTAMPTZ,
    updated_at      TIMESTAMPTZ,

    CONSTRAINT uk_key_service_name UNIQUE (api_key_id, service_name)
);

CREATE INDEX idx_api_keys_value ON api_keys (key_value);