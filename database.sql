CREATE DATABASE IF NOT EXISTS lost_and_found_db;
USE lost_and_found_db;

CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role ENUM('Reporter','Claimer') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    item_name VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    category VARCHAR(50),
    location_found VARCHAR(100),
    date_found DATE,
    reported_by VARCHAR(100),
    status ENUM('Unclaimed','Claim Pending','Claimed') DEFAULT 'Unclaimed',
    claimed_by VARCHAR(100) DEFAULT NULL,
    claim_date TIMESTAMP NULL DEFAULT NULL
);

INSERT INTO users (full_name, username, password, role) VALUES
('Admin Reporter', 'reporter1', 'pass123', 'Reporter'),
('Juan Dela Cruz', 'claimer1', 'pass123', 'Claimer');

INSERT INTO items (item_name, description, category, location_found, date_found, reported_by, status) VALUES
('Black Wallet', 'Leather wallet with school ID', 'Accessories', 'Library 2nd Floor', '2026-04-01', 'Admin Reporter', 'Unclaimed'),
('iPhone 14', 'Rose gold, cracked screen', 'Electronics', 'Cafeteria Table 5', '2026-04-03', 'Admin Reporter', 'Unclaimed'),
('Blue Umbrella', 'Folding umbrella navy blue', 'Others', 'Room 301', '2026-04-05', 'Admin Reporter', 'Unclaimed');
