INSERT INTO users (email) VALUES ('admin@local.test') ON CONFLICT DO NOTHING;

INSERT INTO products (name, quantity_in_stock, unit_price)
VALUES
  ('Laptop', 20, 899.99),
  ('Mouse', 150, 24.50)
ON CONFLICT DO NOTHING;

INSERT INTO customers (first_name, last_name, email)
VALUES
  ('Jane', 'Doe', 'jane@example.com')
ON CONFLICT DO NOTHING;
