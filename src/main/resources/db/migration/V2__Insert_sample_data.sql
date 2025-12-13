-- Insert Sample Categories
INSERT INTO categories (name, description, active) VALUES
('Electronics', 'Electronic devices and gadgets', true),
('Clothing', 'Apparel and fashion items', true),
('Books', 'Books and educational materials', true),
('Sports', 'Sports and outdoor equipment', true),
('Home & Garden', 'Home improvement and garden supplies', true);

-- Insert Sample Products
INSERT INTO products (name, sku, description, price, quantity, active, brand, category_id, weight) VALUES
-- Electronics
('Smartphone Pro Max', 'PHONE-001', 'Latest flagship smartphone with advanced camera system', 999.99, 50, true, 'TechCorp', 1, 0.200),
('Wireless Earbuds', 'AUDIO-002', 'Premium noise-cancelling wireless earbuds', 199.99, 100, true, 'SoundTech', 1, 0.050),
('4K Monitor', 'DISPLAY-003', '32-inch 4K UHD monitor for professional use', 449.99, 25, true, 'ViewTech', 1, 8.500),
('Gaming Laptop', 'LAPTOP-004', 'High-performance gaming laptop with RTX graphics', 1299.99, 15, true, 'GameTech', 1, 2.300),

-- Clothing
('Cotton T-Shirt', 'SHIRT-005', 'Comfortable 100% cotton t-shirt', 19.99, 200, true, 'ComfortWear', 2, 0.150),
('Denim Jeans', 'JEANS-006', 'Classic blue denim jeans', 59.99, 80, true, 'DenimCo', 2, 0.600),
('Running Shoes', 'SHOES-007', 'Lightweight running shoes with cushioned sole', 89.99, 60, true, 'RunFast', 2, 0.800),
('Winter Jacket', 'JACKET-008', 'Waterproof winter jacket with thermal lining', 149.99, 40, true, 'WinterGear', 2, 1.200),

-- Books
('Java Programming Guide', 'BOOK-009', 'Comprehensive guide to Java programming', 39.99, 75, true, 'TechBooks', 3, 0.500),
('Data Structures & Algorithms', 'BOOK-010', 'Essential algorithms and data structures', 49.99, 50, true, 'CodePress', 3, 0.650),
('Spring Boot in Action', 'BOOK-011', 'Practical guide to Spring Boot development', 44.99, 30, true, 'DevBooks', 3, 0.550),

-- Sports
('Tennis Racket', 'TENNIS-012', 'Professional tennis racket for competitive play', 129.99, 20, true, 'SportsPro', 4, 0.300),
('Basketball', 'BALL-013', 'Official size basketball for indoor/outdoor use', 29.99, 100, true, 'BallCorp', 4, 0.600),
('Yoga Mat', 'YOGA-014', 'Non-slip yoga mat for exercise and meditation', 24.99, 150, true, 'FitLife', 4, 1.000),

-- Home & Garden
('Coffee Maker', 'COFFEE-015', 'Programmable coffee maker with thermal carafe', 89.99, 35, true, 'BrewMaster', 5, 3.200),
('Garden Hose', 'HOSE-016', '50ft expandable garden hose with spray nozzle', 34.99, 45, true, 'GardenPro', 5, 2.500),
('LED Desk Lamp', 'LAMP-017', 'Adjustable LED desk lamp with USB charging port', 39.99, 70, true, 'LightTech', 5, 0.800);