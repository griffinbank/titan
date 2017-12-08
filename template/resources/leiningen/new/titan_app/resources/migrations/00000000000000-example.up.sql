CREATE SCHEMA {{name}};

CREATE TABLE {{name}}.user (
    id          SERIAL      PRIMARY KEY,
    email       text        UNIQUE,
    password    text        NOT NULL,
    name        text        NOT NULL,
);

