CREATE TABLE IF NOT EXISTS sample_entity (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

INSERT INTO sample_entity (name) VALUES ('Alpha Sample');
INSERT INTO sample_entity (name) VALUES ('Beta Sample');
