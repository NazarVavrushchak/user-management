CREATE TABLE events(
    id BIGSERIAL PRIMARY KEY,
    title TEXT,
    task_description TEXT NOT NULL,
    task_time TIMESTAMP NOT NULL,
    time_before_notification TIMESTAMP NOT NULL,
    user_id BIGINT REFERENCES users(id),
    note_id BIGINT REFERENCES notes(id)
);