CREATE TABLE monitoring_settings
(
    id              UUID PRIMARY KEY,
    user_id         UUID         NOT NULL,
    account_id      UUID         NOT NULL,
    service_name    VARCHAR(100) NOT NULL,
    is_active       BOOLEAN      NOT NULL DEFAULT TRUE,
    check_min       INTEGER,
    report_hr       INTEGER,
    last_checked_at TIMESTAMPTZ,
    last_report_at  TIMESTAMPTZ,
    created_at      TIMESTAMPTZ,
    updated_at      TIMESTAMPTZ
);

ALTER TABLE monitoring_settings
    ADD CONSTRAINT uk_monitoring_account_service
        UNIQUE (account_id, service_name);