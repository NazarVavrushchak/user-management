CREATE TABLE groups (
    id BIGSERIAL PRIMARY KEY,
    name_of_group TEXT NOT NULL,
    users TEXT,
    roles TEXT,
    user_id BIGINT REFERENCES users(id),
    role_id BIGINT REFERENCES roles(role_id)
);