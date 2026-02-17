CREATE TYPE outbox_status AS ENUM ('PENDING', 'PROCESSING', 'SENT', 'FAILED');

CREATE TABLE outbox_messages
(
    id          UUID PRIMARY KEY,
    topic       VARCHAR(255)  NOT NULL,
    payload     JSONB         NOT NULL,
    status      outbox_status NOT NULL DEFAULT 'PENDING',
    retry_count INTEGER       NOT NULL DEFAULT 0,
    created_at  TIMESTAMPTZ   NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMPTZ,
    error_log   TEXT
);

CREATE INDEX idx_outbox_pending_relay
    ON outbox_messages (created_at ASC)
    WHERE status = 'PENDING';