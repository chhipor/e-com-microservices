CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE IF NOT EXISTS products (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(150) NOT NULL UNIQUE,
    sku VARCHAR(80) NOT NULL UNIQUE,
    description VARCHAR(500),
    unit_price NUMERIC(19,2) NOT NULL,
    quantity_in_stock INTEGER NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    thumbnail VARCHAR(1000),
    images TEXT[] NOT NULL DEFAULT '{}',
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Upgrade databases created before product images existed.
ALTER TABLE products ADD COLUMN IF NOT EXISTS thumbnail VARCHAR(1000);
ALTER TABLE products ADD COLUMN IF NOT EXISTS images TEXT[] NOT NULL DEFAULT '{}';

CREATE TABLE IF NOT EXISTS customers (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID UNIQUE,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    phone VARCHAR(30),
    address VARCHAR(255),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Links a customer record to its auth-service user (JWT userId claim).
ALTER TABLE customers ADD COLUMN IF NOT EXISTS user_id UUID UNIQUE;

CREATE TABLE IF NOT EXISTS orders (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    order_number VARCHAR(40) NOT NULL UNIQUE,
    order_date TIMESTAMP WITH TIME ZONE NOT NULL,
    total_amount NUMERIC(19,2) NOT NULL,
    status VARCHAR(30) NOT NULL,
    customer_id UUID NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_orders_customer FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS order_items (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    order_id UUID NOT NULL,
    product_id UUID NOT NULL,
    quantity INTEGER NOT NULL,
    unit_price NUMERIC(19,2) NOT NULL,
    line_total NUMERIC(19,2) NOT NULL,
    CONSTRAINT fk_order_items_order FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    CONSTRAINT fk_order_items_product FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS carts (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    customer_id UUID NOT NULL UNIQUE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_carts_customer FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS cart_items (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    cart_id UUID NOT NULL,
    product_id UUID NOT NULL,
    quantity INTEGER NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_cart_items_cart FOREIGN KEY (cart_id) REFERENCES carts(id) ON DELETE CASCADE,
    CONSTRAINT fk_cart_items_product FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    CONSTRAINT uk_cart_items_cart_product UNIQUE (cart_id, product_id)
);

CREATE INDEX IF NOT EXISTS idx_orders_customer_id ON orders(customer_id);
CREATE INDEX IF NOT EXISTS idx_order_items_order_id ON order_items(order_id);
CREATE INDEX IF NOT EXISTS idx_cart_items_cart_id ON cart_items(cart_id);

-- Seed data. Product images are hosted by the DummyJSON CDN (https://dummyjson.com) for development.
INSERT INTO products (id, name, sku, description, unit_price, quantity_in_stock, active, thumbnail, images)
VALUES
 ('10000000-0000-0000-0000-000000000001', 'MacBook Pro 14', 'APPLE-MBP14-SG', '14-inch Apple laptop in Space Grey with Liquid Retina XDR display', 1999.00, 18, TRUE,
  'https://cdn.dummyjson.com/product-images/laptops/apple-macbook-pro-14-inch-space-grey/thumbnail.webp',
  ARRAY['https://cdn.dummyjson.com/product-images/laptops/apple-macbook-pro-14-inch-space-grey/1.webp', 'https://cdn.dummyjson.com/product-images/laptops/apple-macbook-pro-14-inch-space-grey/2.webp', 'https://cdn.dummyjson.com/product-images/laptops/apple-macbook-pro-14-inch-space-grey/3.webp']::TEXT[]),
 ('10000000-0000-0000-0000-000000000002', 'Dell XPS 13 9300', 'DELL-XPS13-9300', 'Compact 13.4-inch premium Windows laptop with InfinityEdge display', 1499.00, 24, TRUE,
  'https://cdn.dummyjson.com/product-images/laptops/new-dell-xps-13-9300-laptop/thumbnail.webp',
  ARRAY['https://cdn.dummyjson.com/product-images/laptops/new-dell-xps-13-9300-laptop/1.webp', 'https://cdn.dummyjson.com/product-images/laptops/new-dell-xps-13-9300-laptop/2.webp', 'https://cdn.dummyjson.com/product-images/laptops/new-dell-xps-13-9300-laptop/3.webp']::TEXT[]),
 ('10000000-0000-0000-0000-000000000003', 'iPhone 13 Pro', 'APPLE-IP13P-256', 'Pro smartphone with ProMotion display and triple camera, 256GB', 1099.00, 31, TRUE,
  'https://cdn.dummyjson.com/product-images/smartphones/iphone-13-pro/thumbnail.webp',
  ARRAY['https://cdn.dummyjson.com/product-images/smartphones/iphone-13-pro/1.webp', 'https://cdn.dummyjson.com/product-images/smartphones/iphone-13-pro/2.webp', 'https://cdn.dummyjson.com/product-images/smartphones/iphone-13-pro/3.webp']::TEXT[]),
 ('10000000-0000-0000-0000-000000000004', 'AirPods Max Silver', 'APPLE-APMAX-SLV', 'Over-ear wireless headphones with active noise cancellation', 549.00, 7, TRUE,
  'https://cdn.dummyjson.com/product-images/mobile-accessories/apple-airpods-max-silver/thumbnail.webp',
  ARRAY['https://cdn.dummyjson.com/product-images/mobile-accessories/apple-airpods-max-silver/1.webp']::TEXT[]),
 ('10000000-0000-0000-0000-000000000005', 'Apple Watch Series 4 Gold', 'APPLE-AW4-GLD', 'Smartwatch with ECG, fall detection and gold stainless steel case', 349.00, 42, TRUE,
  'https://cdn.dummyjson.com/product-images/mobile-accessories/apple-watch-series-4-gold/thumbnail.webp',
  ARRAY['https://cdn.dummyjson.com/product-images/mobile-accessories/apple-watch-series-4-gold/1.webp', 'https://cdn.dummyjson.com/product-images/mobile-accessories/apple-watch-series-4-gold/2.webp', 'https://cdn.dummyjson.com/product-images/mobile-accessories/apple-watch-series-4-gold/3.webp']::TEXT[]),
 ('10000000-0000-0000-0000-000000000006', 'Asus Zenbook Pro Duo', 'ASUS-ZBPRO-DUO', 'Dual-screen creator laptop with ScreenPad Plus secondary display', 1799.00, 9, TRUE,
  'https://cdn.dummyjson.com/product-images/laptops/asus-zenbook-pro-dual-screen-laptop/thumbnail.webp',
  ARRAY['https://cdn.dummyjson.com/product-images/laptops/asus-zenbook-pro-dual-screen-laptop/1.webp', 'https://cdn.dummyjson.com/product-images/laptops/asus-zenbook-pro-dual-screen-laptop/2.webp', 'https://cdn.dummyjson.com/product-images/laptops/asus-zenbook-pro-dual-screen-laptop/3.webp']::TEXT[]),
 ('10000000-0000-0000-0000-000000000007', 'Huawei MateBook X Pro', 'HUAWEI-MBXPRO', 'Ultra-slim 13.9-inch laptop with 3K touch FullView display', 1399.00, 14, TRUE,
  'https://cdn.dummyjson.com/product-images/laptops/huawei-matebook-x-pro/thumbnail.webp',
  ARRAY['https://cdn.dummyjson.com/product-images/laptops/huawei-matebook-x-pro/1.webp', 'https://cdn.dummyjson.com/product-images/laptops/huawei-matebook-x-pro/2.webp', 'https://cdn.dummyjson.com/product-images/laptops/huawei-matebook-x-pro/3.webp']::TEXT[]),
 ('10000000-0000-0000-0000-000000000008', 'Lenovo Yoga 920', 'LENOVO-YOGA920', '2-in-1 convertible laptop with 360-degree watchband hinge', 1099.00, 11, TRUE,
  'https://cdn.dummyjson.com/product-images/laptops/lenovo-yoga-920/thumbnail.webp',
  ARRAY['https://cdn.dummyjson.com/product-images/laptops/lenovo-yoga-920/1.webp', 'https://cdn.dummyjson.com/product-images/laptops/lenovo-yoga-920/2.webp', 'https://cdn.dummyjson.com/product-images/laptops/lenovo-yoga-920/3.webp']::TEXT[]),
 ('10000000-0000-0000-0000-000000000009', 'Samsung Galaxy S10', 'SAMSUNG-GS10-128', 'Android smartphone with Infinity-O display and triple camera, 128GB', 699.00, 27, TRUE,
  'https://cdn.dummyjson.com/product-images/smartphones/samsung-galaxy-s10/thumbnail.webp',
  ARRAY['https://cdn.dummyjson.com/product-images/smartphones/samsung-galaxy-s10/1.webp', 'https://cdn.dummyjson.com/product-images/smartphones/samsung-galaxy-s10/2.webp', 'https://cdn.dummyjson.com/product-images/smartphones/samsung-galaxy-s10/3.webp']::TEXT[]),
 ('10000000-0000-0000-0000-000000000010', 'iPhone X', 'APPLE-IPX-64', '5.8-inch Super Retina smartphone with Face ID, 64GB', 899.00, 15, TRUE,
  'https://cdn.dummyjson.com/product-images/smartphones/iphone-x/thumbnail.webp',
  ARRAY['https://cdn.dummyjson.com/product-images/smartphones/iphone-x/1.webp', 'https://cdn.dummyjson.com/product-images/smartphones/iphone-x/2.webp', 'https://cdn.dummyjson.com/product-images/smartphones/iphone-x/3.webp']::TEXT[]),
 ('10000000-0000-0000-0000-000000000011', 'iPad Mini 2021 Starlight', 'APPLE-IPADMINI6-SL', '8.3-inch Liquid Retina tablet with A15 Bionic chip', 499.00, 20, TRUE,
  'https://cdn.dummyjson.com/product-images/tablets/ipad-mini-2021-starlight/thumbnail.webp',
  ARRAY['https://cdn.dummyjson.com/product-images/tablets/ipad-mini-2021-starlight/1.webp', 'https://cdn.dummyjson.com/product-images/tablets/ipad-mini-2021-starlight/2.webp', 'https://cdn.dummyjson.com/product-images/tablets/ipad-mini-2021-starlight/3.webp', 'https://cdn.dummyjson.com/product-images/tablets/ipad-mini-2021-starlight/4.webp']::TEXT[]),
 ('10000000-0000-0000-0000-000000000012', 'Samsung Galaxy Tab S8+ Grey', 'SAMSUNG-TABS8P-GR', '12.4-inch Super AMOLED Android tablet with S Pen included', 599.00, 13, TRUE,
  'https://cdn.dummyjson.com/product-images/tablets/samsung-galaxy-tab-s8-plus-grey/thumbnail.webp',
  ARRAY['https://cdn.dummyjson.com/product-images/tablets/samsung-galaxy-tab-s8-plus-grey/1.webp', 'https://cdn.dummyjson.com/product-images/tablets/samsung-galaxy-tab-s8-plus-grey/2.webp', 'https://cdn.dummyjson.com/product-images/tablets/samsung-galaxy-tab-s8-plus-grey/3.webp', 'https://cdn.dummyjson.com/product-images/tablets/samsung-galaxy-tab-s8-plus-grey/4.webp']::TEXT[]),
 ('10000000-0000-0000-0000-000000000013', 'AirPods', 'APPLE-AIRPODS', 'True wireless earbuds with charging case and automatic pairing', 129.00, 60, TRUE,
  'https://cdn.dummyjson.com/product-images/mobile-accessories/apple-airpods/thumbnail.webp',
  ARRAY['https://cdn.dummyjson.com/product-images/mobile-accessories/apple-airpods/1.webp', 'https://cdn.dummyjson.com/product-images/mobile-accessories/apple-airpods/2.webp', 'https://cdn.dummyjson.com/product-images/mobile-accessories/apple-airpods/3.webp']::TEXT[]),
 ('10000000-0000-0000-0000-000000000014', 'HomePod mini Space Grey', 'APPLE-HPMINI-SG', 'Compact smart speaker with 360-degree audio and Siri', 99.00, 35, TRUE,
  'https://cdn.dummyjson.com/product-images/mobile-accessories/apple-homepod-mini-cosmic-grey/thumbnail.webp',
  ARRAY['https://cdn.dummyjson.com/product-images/mobile-accessories/apple-homepod-mini-cosmic-grey/1.webp']::TEXT[]),
 ('10000000-0000-0000-0000-000000000015', 'Amazon Echo Plus', 'AMAZON-ECHOPLUS', 'Smart speaker with Alexa and built-in smart home hub', 99.00, 0, TRUE,
  'https://cdn.dummyjson.com/product-images/mobile-accessories/amazon-echo-plus/thumbnail.webp',
  ARRAY['https://cdn.dummyjson.com/product-images/mobile-accessories/amazon-echo-plus/1.webp', 'https://cdn.dummyjson.com/product-images/mobile-accessories/amazon-echo-plus/2.webp']::TEXT[])
ON CONFLICT DO NOTHING;

INSERT INTO customers (id, first_name, last_name, email, phone, address)
VALUES
 ('20000000-0000-0000-0000-000000000001', 'Sophia', 'Carter', 'sophia.carter@example.com', '+1-202-555-0101', '100 Market Street, San Francisco, CA'),
 ('20000000-0000-0000-0000-000000000002', 'Liam', 'Nguyen', 'liam.nguyen@example.com', '+1-202-555-0102', '25 King Street, Seattle, WA'),
 ('20000000-0000-0000-0000-000000000003', 'Olivia', 'Williams', 'olivia.williams@example.com', '+1-202-555-0103', '48 Broadway, New York, NY')
ON CONFLICT DO NOTHING;

INSERT INTO orders (id, order_number, order_date, total_amount, status, customer_id)
VALUES
 ('30000000-0000-0000-0000-000000000001', 'ORD-2026-0001', CURRENT_TIMESTAMP - INTERVAL '18 days', 1999.00, 'COMPLETED', '20000000-0000-0000-0000-000000000001'),
 ('30000000-0000-0000-0000-000000000002', 'ORD-2026-0002', CURRENT_TIMESTAMP - INTERVAL '7 days', 1848.00, 'COMPLETED', '20000000-0000-0000-0000-000000000002'),
 ('30000000-0000-0000-0000-000000000003', 'ORD-2026-0003', CURRENT_TIMESTAMP - INTERVAL '1 day', 1099.00, 'PENDING', '20000000-0000-0000-0000-000000000003')
ON CONFLICT DO NOTHING;

INSERT INTO order_items (id, order_id, product_id, quantity, unit_price, line_total)
VALUES
 ('40000000-0000-0000-0000-000000000001', '30000000-0000-0000-0000-000000000001', '10000000-0000-0000-0000-000000000001', 1, 1999.00, 1999.00),
 ('40000000-0000-0000-0000-000000000002', '30000000-0000-0000-0000-000000000002', '10000000-0000-0000-0000-000000000002', 1, 1499.00, 1499.00),
 ('40000000-0000-0000-0000-000000000003', '30000000-0000-0000-0000-000000000002', '10000000-0000-0000-0000-000000000005', 1, 349.00, 349.00),
 ('40000000-0000-0000-0000-000000000004', '30000000-0000-0000-0000-000000000003', '10000000-0000-0000-0000-000000000003', 1, 1099.00, 1099.00)
ON CONFLICT DO NOTHING;
