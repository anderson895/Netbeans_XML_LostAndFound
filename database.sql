CREATE DATABASE IF NOT EXISTS lost_and_found_db;
USE lost_and_found_db;

DROP TABLE IF EXISTS claim_requests;
DROP TABLE IF EXISTS items;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role ENUM('Admin','User') NOT NULL DEFAULT 'User',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    item_name VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    category VARCHAR(50),
    type ENUM('Lost','Found') NOT NULL,
    location VARCHAR(100),
    date_reported DATE,
    reported_by INT NOT NULL,
    status ENUM('Open','Claim Pending','Claimed','Resolved') DEFAULT 'Open',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (reported_by) REFERENCES users(id)
);

CREATE TABLE claim_requests (
    id INT AUTO_INCREMENT PRIMARY KEY,
    item_id INT NOT NULL,
    requested_by INT NOT NULL,
    message VARCHAR(255),
    status ENUM('Pending','Approved','Rejected') DEFAULT 'Pending',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    resolved_at TIMESTAMP NULL,
    FOREIGN KEY (item_id) REFERENCES items(id) ON DELETE CASCADE,
    FOREIGN KEY (requested_by) REFERENCES users(id)
);

-- Default admin account (username: admin, password: admin123)
INSERT INTO users (full_name, username, password, role) VALUES
('System Administrator', 'admin', 'admin123', 'Admin');

-- Sample user
INSERT INTO users (full_name, username, password, role) VALUES
('Juan Dela Cruz', 'juan', 'pass123', 'User');

-- Sample items
INSERT INTO items (item_name, description, category, type, location, date_reported, reported_by, status) VALUES
('Black Wallet', 'Leather wallet with school ID inside', 'Accessories', 'Found', 'Library 2nd Floor', '2026-04-01', 1, 'Open'),
('iPhone 14', 'Rose gold, cracked screen protector', 'Electronics', 'Lost', 'Cafeteria', '2026-04-03', 2, 'Open'),
('Blue Umbrella', 'Folding umbrella, navy blue color', 'Others', 'Found', 'Room 301', '2026-04-05', 1, 'Open');
