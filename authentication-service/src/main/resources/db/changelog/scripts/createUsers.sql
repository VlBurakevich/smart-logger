CREATE TABLE users
(
    id         UUID PRIMARY KEY,
    account_id UUID UNIQUE NOT NULL,
    is_active  BOOLEAN   DEFAULT TRUE,
    username       VARCHAR(100),
    created_at TIMESTAMPTZ DEFAULT NOW()
);