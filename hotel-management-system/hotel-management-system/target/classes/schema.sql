-- Hotel Management System Database Setup
-- MySQL Database Initialization Script

-- Create database (if not exists)
CREATE DATABASE IF NOT EXISTS hotel_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE hotel_db;

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

-- Cards Table
CREATE TABLE IF NOT EXISTS cards (
	card_id VARCHAR(255) PRIMARY KEY,
	user_id VARCHAR(255) NOT NULL,
	card_number VARCHAR(12) NOT NULL,
	card_holder_name VARCHAR(100) NOT NULL,
	cvv VARCHAR(3) NOT NULL,
	expiry_date VARCHAR(5) NOT NULL,
	card_type VARCHAR(10) NOT NULL,
	is_default BOOLEAN DEFAULT FALSE,
	created_at DATETIME,
	updated_at DATETIME,
	FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
	UNIQUE KEY unique_user_card (user_id, card_number),
	KEY idx_user_id (user_id),
	KEY idx_is_default (is_default)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;