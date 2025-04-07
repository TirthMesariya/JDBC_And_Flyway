CREATE TABLE customer(
    id BIGSERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    email TEXT NOT NULL,
    phone INT NOT NULL,
    age INT NOT NULL
)