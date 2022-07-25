CREATE TABLE IF NOT EXISTS customers (
    id SERIAL PRIMARY KEY,
    name varchar(50) NOT NULL,
    email varchar(50) NOT NULL,
    department varchar(50) NOT NULL,
    modified TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX name ON customers using HASH (modified);