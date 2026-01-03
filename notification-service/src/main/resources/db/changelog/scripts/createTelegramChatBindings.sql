CREATE TABLE telegram_chat_bindings
(
    user_id              uuid PRIMARY KEY,
    chat_id              BIGINT UNIQUE NOT NULL,
    username             VARCHAR(255),
    first_name           VARCHAR(255),
    created_at           TIMESTAMPTZ DEFAULT NOW(),
    last_notification_at TIMESTAMPTZ
)