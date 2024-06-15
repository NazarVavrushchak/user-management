CREATE TABLE roles (
    role_id BIGSERIAL PRIMARY KEY,
    role TEXT NOT NULL
);
-- TODO make rename persons to users
-- TODO make one to many relation between users and roles
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    first_name TEXT NOT NULL,
    last_name TEXT NOT NULL,
    email TEXT NOT NULL,
    age INT NOT NULL,
    role_id BIGINT NOT NULL,
    FOREIGN KEY (role_id) REFERENCES roles(role_id)
);

CREATE TABLE permissions (
                             id BIGSERIAL PRIMARY KEY,
                             first_name TEXT NOT NULL,
                             last_name TEXT NOT NULL,
                             email TEXT NOT NULL,
                             age INT NOT NULL,
                             role_id BIGINT NOT NULL,
                             FOREIGN KEY (role_id) REFERENCES roles(role_id)
);