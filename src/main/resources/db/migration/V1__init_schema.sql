-- 1. Create users table
CREATE TABLE users (
    id UUID PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255),
    role VARCHAR(50) NOT NULL DEFAULT 'USER',
    created_at TIMESTAMP WITHOUT TIME ZONE,
    updated_at TIMESTAMP WITHOUT TIME ZONE
);

-- 2. Create billing_address table
CREATE TABLE billing_address (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL REFERENCES users(id),
    name VARCHAR(255),
    tax_id VARCHAR(255),
    postal_code VARCHAR(255),
    street VARCHAR(255),
    number VARCHAR(255),
    complement VARCHAR(255),
    neighborhood VARCHAR(255),
    city VARCHAR(255),
    uf VARCHAR(255),
    phone VARCHAR(255),
    email VARCHAR(255),
    is_shipping BOOLEAN NOT NULL DEFAULT FALSE
);

-- 3. Create event table
CREATE TABLE event (
    id UUID PRIMARY KEY,
    name VARCHAR(255),
    event_date TIMESTAMP WITH TIME ZONE
);

-- 4. Create ticket_type table
CREATE TABLE ticket_type (
    id UUID PRIMARY KEY,
    event_id UUID REFERENCES event(id),
    name VARCHAR(255),
    price NUMERIC(19, 2),
    quantity_available BIGINT
);

-- 5. Create orders table
CREATE TABLE orders (
    id UUID PRIMARY KEY,
    user_id UUID REFERENCES users(id),
    ticket_type_id UUID REFERENCES ticket_type(id),
    quantity INTEGER,
    total_amount NUMERIC(19, 2),
    status VARCHAR(50),
    billing_address_id UUID REFERENCES billing_address(id),
    created_at TIMESTAMP WITH TIME ZONE
);

-- 6. Create ticket table
CREATE TABLE ticket (
    id UUID PRIMARY KEY,
    ticket_code VARCHAR(255) UNIQUE,
    event_id UUID REFERENCES event(id),
    price_paid NUMERIC(19, 2),
    ticket_type_id UUID REFERENCES ticket_type(id),
    status VARCHAR(50),
    purchase_date TIMESTAMP WITH TIME ZONE,
    order_id UUID REFERENCES orders(id)
);
