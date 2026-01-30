CREATE TABLE users
(
    id         UUID PRIMARY KEY,
    is_active  BOOLEAN   DEFAULT TRUE,
    username       VARCHAR(100),
    created_at TIMESTAMPTZ DEFAULT NOW()
);