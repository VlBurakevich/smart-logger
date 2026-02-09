CREATE TYPE task_status AS ENUM ('READY', 'PROCESSING', 'ERROR');

CREATE TABLE monitoring_tasks
(
    id              UUID PRIMARY KEY,
    api_key_id      UUID                        NOT NULL REFERENCES api_keys (id) ON DELETE CASCADE,
    service_name    VARCHAR(100)                NOT NULL,
    status          task_status DEFAULT 'READY' NOT NULL,
    check_sec       INTEGER,
    report_hr       INTEGER,
    last_checked_at TIMESTAMPTZ,
    last_report_at  TIMESTAMPTZ,
    created_at      TIMESTAMPTZ,
    updated_at      TIMESTAMPTZ,
    version         BIGINT,

    CONSTRAINT uk_key_service_name UNIQUE (api_key_id, service_name)
);

CREATE INDEX idx_monitoring_tasks_snapshot_scheduler ON monitoring_tasks (status, last_checked_at);
CREATE INDEX idx_monitoring_tasks_report_scheduler ON monitoring_tasks (status, last_report_at);