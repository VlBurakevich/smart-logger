CREATE TABLE users
(
    id        UUID PRIMARY KEY,
    username  VARCHAR(100),
    is_active BOOLEAN NOT NULL DEFAULT TRUE
);