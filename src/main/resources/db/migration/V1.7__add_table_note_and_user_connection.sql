CREATE TABLE note_members (
    note_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    PRIMARY KEY (note_id, user_id),
    FOREIGN KEY (note_id) REFERENCES notes(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);