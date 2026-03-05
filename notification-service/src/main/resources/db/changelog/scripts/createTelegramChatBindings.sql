CREATE TABLE telegram_bindings
(
    id                   UUID PRIMARY KEY,
    user_id              UUID NOT NULL,
    chat_id              BIGINT UNIQUE NOT NULL,
    username             VARCHAR(255),
    first_name           VARCHAR(255),
    created_at           TIMESTAMPTZ DEFAULT NOW(),
    last_notification_at TIMESTAMPTZ
);

CREATE INDEX idx_telegram_bindings_user_id ON telegram_bindings (user_id);
