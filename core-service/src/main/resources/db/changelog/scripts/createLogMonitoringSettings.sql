CREATE TABLE log_monitoring_settings
(
    account_id      uuid PRIMARY KEY,
    service_name    VARCHAR(100) NOT NULL,
    check_min       INTEGER,
    report_hr       INTEGER,
    last_checked_at TIMESTAMPTZ,
    last_report_at  TIMESTAMPTZ,
    active          BOOLEAN DEFAULT TRUE,
    created_at      TIMESTAMPTZ,
    updated_at      TIMESTAMPTZ
);