CREATE TABLE IF NOT EXISTS payments (
    id SERIAL PRIMARY KEY,
    payment_identifier VARCHAR(255) UNIQUE NOT NULL,
    amount DOUBLE PRECISION,
    method VARCHAR(50),
    status VARCHAR(50),
    timestamp TIMESTAMPTZ,
    transaction_ref VARCHAR(255)
);
