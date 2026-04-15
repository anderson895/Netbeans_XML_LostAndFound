CREATE DATABASE IF NOT EXISTS lost_and_found_db;
USE lost_and_found_db;

DROP TABLE IF EXISTS claim_requests;
DROP TABLE IF EXISTS deleted_items;
DROP TABLE IF EXISTS items;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(150) NOT NULL,
    student_id VARCHAR(20) NOT NULL UNIQUE,
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
    verification_question VARCHAR(255) DEFAULT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (reported_by) REFERENCES users(id)
);

-- Recycle bin for soft-deleted items (single entry restore)
CREATE TABLE deleted_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    original_id INT NOT NULL,
    item_name VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    category VARCHAR(50),
    type VARCHAR(10) NOT NULL,
    location VARCHAR(100),
    date_reported DATE,
    reported_by INT NOT NULL,
    status VARCHAR(30),
    verification_question VARCHAR(255),
    deleted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (reported_by) REFERENCES users(id)
);

CREATE TABLE claim_requests (
    id INT AUTO_INCREMENT PRIMARY KEY,
    item_id INT NOT NULL,
    requested_by INT NOT NULL,
    message VARCHAR(255),
    verification_answer VARCHAR(255) DEFAULT NULL,
    status ENUM('Pending','Approved','Rejected') DEFAULT 'Pending',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    resolved_at TIMESTAMP NULL,
    FOREIGN KEY (item_id) REFERENCES items(id) ON DELETE CASCADE,
    FOREIGN KEY (requested_by) REFERENCES users(id)
);

-- Default admin account (student_id: 21-00-0001, password: admin123)
INSERT INTO users (full_name, student_id, password, role) VALUES
('Administrator, System, A.', '21-00-0001', 'admin123', 'Admin');

-- Sample user (format: Last Name, Given Name, Middle Initial)
INSERT INTO users (full_name, student_id, password, role) VALUES
('De Asis, Diana Kyla, I.', '21-01-1155', 'pass123', 'User');

-- Sample items with verification questions on Found items
INSERT INTO items (item_name, description, category, type, location, date_reported, reported_by, status, verification_question) VALUES
('Black Wallet', 'Leather wallet with school ID inside', 'Accessories', 'Found', 'ASCOT ENTRANCE', '2026-04-01', 1, 'Open', 'What color is the school ID inside?'),
('iPhone 14', 'Rose gold, cracked screen protector', 'Electronics', 'Lost', 'Engineering Building', '2026-04-03', 2, 'Open', NULL),
('Blue Umbrella', 'Folding umbrella, navy blue color', 'Others', 'Found', 'General Education Buildings', '2026-04-05', 1, 'Open', 'What brand is the umbrella?');
