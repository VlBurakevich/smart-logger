CREATE TABLE user_role
(
    user_id   UUID REFERENCES users (id) ON DELETE CASCADE,
    role_name VARCHAR(32) REFERENCES roles (name) ON DELETE CASCADE

    PRIMARY KEY (user_id, role_name)
);