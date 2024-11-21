DROP TABLE IF EXISTS users, task, comment;

CREATE TABLE IF NOT EXISTS users
(
    id       BIGSERIAL PRIMARY KEY,
    name     varchar(255) NOT NULL,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role     VARCHAR      NOT NULL
);

CREATE TABLE IF NOT EXISTS task
(
    id             BIGSERIAL PRIMARY KEY,
    title          VARCHAR(255) NOT NULL,
    description    TEXT,
    status         VARCHAR(20),
    priority       VARCHAR(20),
    author_email   VARCHAR(255),
    assignee_email VARCHAR(255),
    created_at     TIMESTAMP,
    updated_at     TIMESTAMP
);

CREATE TABLE IF NOT EXISTS comment
(
    id             BIGSERIAL PRIMARY KEY,
    text           TEXT NOT NULL,
    assignee_email VARCHAR(255),
    created_at     TIMESTAMP,
    updated_at     TIMESTAMP,
    task_id        BIGINT,
    FOREIGN KEY (task_id) REFERENCES task (id) ON DELETE CASCADE
);

