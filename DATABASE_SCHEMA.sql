-- Quick Commerce Delivery System - Database Schema
-- MySQL Database Script
-- Script Version: 1.0

-- Create Database
CREATE DATABASE IF NOT EXISTS quick_commerce;
USE quick_commerce;

-- ============================================================================
-- MODULE 4: USER & AUTHENTICATION & PROFILE
-- ============================================================================

CREATE TABLE users (
    user_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    phone_number VARCHAR(20) NOT NULL UNIQUE,
    role ENUM('CUSTOMER', 'DELIVERY_PARTNER', 'ADMIN', 'STORE_MANAGER') NOT NULL,
    address VARCHAR(500) NOT NULL,
    city VARCHAR(100) NOT NULL,
    zip_code VARCHAR(10) NOT NULL,
    wallet_balance DECIMAL(10,2) DEFAULT 0.00,
    is_active BOOLEAN DEFAULT TRUE,
    is_verified BOOLEAN DEFAULT FALSE,
    profile_image_url VARCHAR(500),
    average_rating DOUBLE DEFAULT 0,
    total_orders INT DEFAULT 0,
    vehicle_number VARCHAR(50),
    license_number VARCHAR(50),
    delivery_rating DOUBLE DEFAULT 0,
    total_deliveries INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_email (email),
    INDEX idx_role (role),
    INDEX idx_is_active (is_active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================================
-- MODULE 1: INVENTORY & CATALOG
-- ============================================================================

CREATE TABLE products (
    product_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    stock INT NOT NULL,
    category VARCHAR(100) NOT NULL,
    subcategory VARCHAR(100) NOT NULL,
    image_url VARCHAR(500),
    rating DOUBLE DEFAULT 0,
    review_count INT DEFAULT 0,
    is_available BOOLEAN DEFAULT TRUE,
    manufacturer VARCHAR(100),
    expiry_days INT,
    is_veg BOOLEAN,
    barcode VARCHAR(50) UNIQUE,
    min_stock_level INT DEFAULT 10,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_category (category),
    INDEX idx_subcategory (subcategory),
    INDEX idx_is_available (is_available),
    INDEX idx_stock (stock),
    INDEX idx_barcode (barcode)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================================
-- MODULE 2: CART MANAGEMENT
-- ============================================================================

CREATE TABLE carts (
    cart_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL UNIQUE,
    total_price DECIMAL(10,2) DEFAULT 0.00,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    INDEX idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE cart_items (
    cart_item_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    cart_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    total_price DECIMAL(10,2),
    FOREIGN KEY (cart_id) REFERENCES carts(cart_id),
    FOREIGN KEY (product_id) REFERENCES products(product_id),
    INDEX idx_cart_id (cart_id),
    INDEX idx_product_id (product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================================
-- MODULE 2: ORDER & CHECKOUT
-- ============================================================================

CREATE TABLE orders (
    order_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    tax_amount DECIMAL(10,2) NOT NULL,
    discount_amount DECIMAL(10,2) NOT NULL,
    delivery_charge DECIMAL(10,2) NOT NULL,
    total_amount DECIMAL(10,2) NOT NULL,
    order_status ENUM('PENDING', 'CONFIRMED', 'PACKING', 'PACKED', 'OUT_FOR_DELIVERY', 'DELIVERED', 'CANCELLED', 'RETURNED') NOT NULL,
    payment_method ENUM('UPI', 'CARD', 'COD', 'WALLET') NOT NULL,
    delivery_address VARCHAR(500),
    delivery_city VARCHAR(100),
    delivery_zip_code VARCHAR(10),
    delivery_partner_id BIGINT,
    tracking_number VARCHAR(100) UNIQUE,
    payment_status BOOLEAN DEFAULT FALSE,
    special_instructions TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    estimated_delivery_time TIMESTAMP,
    actual_delivery_time TIMESTAMP,
    delivery_rating DOUBLE,
    delivery_review TEXT,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (delivery_partner_id) REFERENCES users(user_id),
    INDEX idx_user_id (user_id),
    INDEX idx_order_status (order_status),
    INDEX idx_tracking_number (tracking_number),
    INDEX idx_delivery_partner_id (delivery_partner_id),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE order_items (
    order_item_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    price_at_time DECIMAL(10,2) NOT NULL,
    total_price DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(order_id),
    FOREIGN KEY (product_id) REFERENCES products(product_id),
    INDEX idx_order_id (order_id),
    INDEX idx_product_id (product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================================
-- MODULE 2: DISCOUNTS & OFFERS
-- ============================================================================

CREATE TABLE discounts (
    discount_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    discount_code VARCHAR(50) NOT NULL UNIQUE,
    discount_name VARCHAR(255) NOT NULL,
    description TEXT,
    discount_type ENUM('PERCENTAGE', 'FIXED_AMOUNT', 'FIRST_ORDER', 'SEASONAL') NOT NULL,
    discount_value DECIMAL(10,2) NOT NULL,
    max_discount_amount DECIMAL(10,2),
    min_order_amount DECIMAL(10,2),
    max_usage_count INT NOT NULL,
    current_usage_count INT DEFAULT 0,
    is_active BOOLEAN DEFAULT TRUE,
    valid_from TIMESTAMP NOT NULL,
    valid_until TIMESTAMP NOT NULL,
    applicable_categories VARCHAR(500),
    is_first_order_only BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_discount_code (discount_code),
    INDEX idx_is_active (is_active),
    INDEX idx_valid_until (valid_until)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================================
-- MODULE 3: DELIVERY & TRACKING
-- ============================================================================

CREATE TABLE deliveries (
    delivery_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id BIGINT NOT NULL UNIQUE,
    delivery_partner_id BIGINT NOT NULL,
    pickup_latitude DOUBLE NOT NULL,
    pickup_longitude DOUBLE NOT NULL,
    delivery_latitude DOUBLE NOT NULL,
    delivery_longitude DOUBLE NOT NULL,
    current_latitude DOUBLE,
    current_longitude DOUBLE,
    delivery_status ENUM('PENDING', 'CONFIRMED', 'PACKING', 'PACKED', 'OUT_FOR_DELIVERY', 'DELIVERED', 'CANCELLED', 'RETURNED') NOT NULL,
    estimated_delivery_time_seconds BIGINT,
    estimated_distance_meters BIGINT,
    assigned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    picked_up_at TIMESTAMP,
    delivered_at TIMESTAMP,
    delivery_notes TEXT,
    delivery_proof_image_url VARCHAR(500),
    requires_signature BOOLEAN DEFAULT FALSE,
    is_delayed BOOLEAN DEFAULT FALSE,
    delay_reason VARCHAR(500),
    FOREIGN KEY (order_id) REFERENCES orders(order_id),
    FOREIGN KEY (delivery_partner_id) REFERENCES users(user_id),
    INDEX idx_order_id (order_id),
    INDEX idx_delivery_partner_id (delivery_partner_id),
    INDEX idx_delivery_status (delivery_status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE order_tracking (
    tracking_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id BIGINT NOT NULL,
    previous_status ENUM('PENDING', 'CONFIRMED', 'PACKING', 'PACKED', 'OUT_FOR_DELIVERY', 'DELIVERED', 'CANCELLED', 'RETURNED') NOT NULL,
    current_status ENUM('PENDING', 'CONFIRMED', 'PACKING', 'PACKED', 'OUT_FOR_DELIVERY', 'DELIVERED', 'CANCELLED', 'RETURNED') NOT NULL,
    remarks VARCHAR(500),
    status_changed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    notification_sent VARCHAR(100),
    FOREIGN KEY (order_id) REFERENCES orders(order_id),
    INDEX idx_order_id (order_id),
    INDEX idx_status_changed_at (status_changed_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================================
-- MODULE 4: WALLET & PAYMENTS
-- ============================================================================

CREATE TABLE wallet_transactions (
    transaction_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    transaction_type ENUM('DEBIT', 'CREDIT') NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    description VARCHAR(500),
    reference_order_id VARCHAR(100),
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    transaction_status ENUM('PENDING', 'COMPLETED', 'FAILED') DEFAULT 'COMPLETED',
    payment_gateway_transaction_id VARCHAR(100),
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    INDEX idx_user_id (user_id),
    INDEX idx_transaction_date (transaction_date),
    INDEX idx_transaction_type (transaction_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================================
-- SAMPLE DATA
-- ============================================================================

-- Insert sample users
INSERT INTO users (email, password, full_name, phone_number, role, address, city, zip_code) VALUES
('customer1@gmail.com', 'pass123', 'Rahul Kumar', '9876543210', 'CUSTOMER', '123 Delhi Road', 'Delhi', '110001'),
('partner1@gmail.com', 'pass123', 'Delivery Boy', '9876543211', 'DELIVERY_PARTNER', '456 Delivery Lane', 'Delhi', '110002'),
('admin@gmail.com', 'admin123', 'Admin User', '9876543212', 'ADMIN', '789 Admin St', 'Delhi', '110003');

-- Insert sample products
INSERT INTO products (product_name, description, price, stock, category, subcategory, is_available) VALUES
('Milk 500ml', 'Fresh dairy milk', 50.00, 100, 'Dairy', 'Milk', TRUE),
('Bread Sliced White', 'Slice bread - 400g', 30.00, 50, 'Bakery', 'Bread', TRUE),
('Butter 100g', 'Pure cow butter', 120.00, 30, 'Dairy', 'Butter', TRUE),
('Apple 1kg', 'Fresh red apples', 80.00, 25, 'Fruits', 'Apples', TRUE);

-- ============================================================================
-- CREATE INDEXES for better query performance
-- ============================================================================

CREATE INDEX idx_orders_user_id_status ON orders(user_id, order_status);
CREATE INDEX idx_orders_created_at ON orders(created_at);
CREATE INDEX idx_products_category_stock ON products(category, stock);
CREATE INDEX idx_cart_items_cart_product ON cart_items(cart_id, product_id);
CREATE INDEX idx_order_items_order_product ON order_items(order_id, product_id);
CREATE INDEX idx_deliveries_partner_status ON deliveries(delivery_partner_id, delivery_status);
