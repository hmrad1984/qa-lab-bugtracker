CREATE TABLE IF NOT EXISTS bug_report (
    id SERIAL PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    description TEXT NOT NULL,
    status VARCHAR(50) NOT NULL,
    severity VARCHAR(10) NOT NULL
);
