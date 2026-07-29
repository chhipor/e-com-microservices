INSERT INTO products (name, sku, description, unit_price, quantity_in_stock, active, created_at, updated_at)
VALUES
  ('Laptop', 'LAP-001', 'Business laptop', 899.99, 25, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('Mouse', 'MOU-001', 'Wireless mouse', 24.50, 100, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT DO NOTHING;

INSERT INTO customers (first_name, last_name, email, phone, address, created_at, updated_at)
VALUES
  ('Jane', 'Doe', 'jane@example.com', '012345678', 'Phnom Penh', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT DO NOTHING;
