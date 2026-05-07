CREATE TABLE IF NOT EXISTS order_history (
    order_number VARCHAR(80) PRIMARY KEY,
    order_date TIMESTAMP NOT NULL,
    user_id UUID NOT NULL,
    user_name VARCHAR(255) NOT NULL,
    total_price INTEGER NOT NULL CHECK (total_price >= 0),
    total_quantity INTEGER NOT NULL CHECK (total_quantity >= 0),
    earned_points INTEGER NOT NULL CHECK (earned_points >= 0),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_order_history_user_id_order_date
    ON order_history (user_id, order_date DESC);

CREATE TABLE IF NOT EXISTS order_details (
    id BIGSERIAL PRIMARY KEY,
    order_number VARCHAR(80) NOT NULL REFERENCES order_history (order_number) ON DELETE CASCADE,
    product_id INTEGER NOT NULL,
    product_name VARCHAR(255) NOT NULL,
    price INTEGER NOT NULL CHECK (price >= 0),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_order_details_order_number
    ON order_details (order_number);

CREATE TABLE IF NOT EXISTS member_status (
    user_id UUID PRIMARY KEY,
    total_points INTEGER NOT NULL CHECK (total_points >= 0),
    rank VARCHAR(32) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
