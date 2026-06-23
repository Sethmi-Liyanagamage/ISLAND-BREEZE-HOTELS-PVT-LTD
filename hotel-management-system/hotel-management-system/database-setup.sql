-- Hotel Management System Database Setup
-- MySQL Database Initialization Script

-- Create database (if not exists)
CREATE DATABASE IF NOT EXISTS hotel_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE hotel_db;

-- Note: Tables will be auto-created by Hibernate with spring.jpa.hibernate.ddl-auto=update
-- This script is for reference and manual setup if needed

-- Users Table
CREATE TABLE IF NOT EXISTS users (
    id VARCHAR(255) PRIMARY KEY,
    full_name VARCHAR(255),
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50),
    phone VARCHAR(20)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Admins Table
CREATE TABLE IF NOT EXISTS admins (
    id VARCHAR(255) PRIMARY KEY,
    full_name VARCHAR(255),
    email VARCHAR(255) UNIQUE,
    username VARCHAR(255) UNIQUE,
    password VARCHAR(255),
    role VARCHAR(50),
    created_at VARCHAR(255)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Rooms Table
CREATE TABLE IF NOT EXISTS rooms (
    id VARCHAR(255) PRIMARY KEY,
    room_number VARCHAR(50) UNIQUE NOT NULL,
    type VARCHAR(100),
    price_per_night DOUBLE,
    capacity INT,
    status VARCHAR(50),
    description VARCHAR(1000)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Bookings Table
CREATE TABLE IF NOT EXISTS bookings (
    booking_id VARCHAR(255) PRIMARY KEY,
    guest_id VARCHAR(255),
    room_id VARCHAR(255),
    check_in_date DATE,
    check_out_date DATE,
    total_amount DOUBLE,
    number_of_guests INT,
    status VARCHAR(50),
    guest_name VARCHAR(255),
    room_details VARCHAR(255),
    created_at VARCHAR(255)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Payments Table
CREATE TABLE IF NOT EXISTS payments (
    payment_id VARCHAR(255) PRIMARY KEY,
    booking_id VARCHAR(255),
    guest_id VARCHAR(255),
    amount DOUBLE,
    status VARCHAR(50),
    payment_method VARCHAR(100),
    payment_date DATETIME
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Reviews Table
CREATE TABLE IF NOT EXISTS reviews (
    review_id VARCHAR(255) PRIMARY KEY,
    booking_id VARCHAR(255),
    guest_id VARCHAR(255),
    rating INT,
    comment VARCHAR(2000),
    created_at DATETIME,
    status VARCHAR(50),
    admin_reply VARCHAR(1000)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Create contact_messages table if not exists
CREATE TABLE IF NOT EXISTS contact_messages (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    subject VARCHAR(200) NOT NULL,
    message TEXT NOT NULL,
    urgent BOOLEAN DEFAULT FALSE,
    submitted_at DATETIME NOT NULL,
    replied BOOLEAN DEFAULT FALSE,
    admin_reply TEXT,
    replied_at DATETIME,
    KEY idx_submitted_at (submitted_at),
    KEY idx_replied (replied)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Insert Sample Data

-- Sample Admin
INSERT INTO admins (id, full_name, email, username, password, role, created_at) 
VALUES 
('admin-001', 'System Administrator', 'admin@islandbreeze.com', 'admin', 'admin123', 'ADMIN', NOW())
ON DUPLICATE KEY UPDATE id=id;

-- Sample Rooms
INSERT INTO rooms (id, room_number, type, price_per_night, capacity, status, description) 
VALUES 
('room-001', '101', 'Deluxe Suite', 150.00, 2, 'AVAILABLE', 'Spacious deluxe suite with ocean view'),
('room-002', '102', 'Standard Room', 80.00, 2, 'AVAILABLE', 'Comfortable standard room'),
('room-003', '201', 'Family Suite', 220.00, 4, 'AVAILABLE', 'Large family suite with two bedrooms'),
('room-004', '202', 'Presidential Suite', 350.00, 4, 'AVAILABLE', 'Luxury presidential suite with premium amenities'),
('room-005', '301', 'Standard Room', 80.00, 2, 'OCCUPIED', 'Comfortable standard room')
ON DUPLICATE KEY UPDATE id=id;

-- Sample Guest User
INSERT INTO users (id, full_name, email, password, role, phone) 
VALUES 
('user-001', 'John Doe', 'john@example.com', 'password123', 'GUEST', '+1234567890')
ON DUPLICATE KEY UPDATE id=id;

-- Indexes for better performance
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_rooms_status ON rooms(status);
CREATE INDEX idx_bookings_guest ON bookings(guest_id);
CREATE INDEX idx_bookings_room ON bookings(room_id);
CREATE INDEX idx_payments_guest ON payments(guest_id);
CREATE INDEX idx_reviews_guest ON reviews(guest_id);

-- Display table information
SHOW TABLES;

SELECT 'Database setup complete!' AS status;
