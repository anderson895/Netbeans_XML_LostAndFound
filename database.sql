CREATE DATABASE IF NOT EXISTS lost_and_found_db;
USE lost_and_found_db;

DROP TABLE IF EXISTS audit_trail;
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
    course VARCHAR(80) DEFAULT NULL,
    year_level VARCHAR(20) DEFAULT NULL,
    section VARCHAR(20) DEFAULT NULL,
    profile_picture MEDIUMBLOB,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    item_name VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    category VARCHAR(50),
    type ENUM('Lost','Found') NOT NULL,
    location VARCHAR(150),
    date_reported DATETIME,
    reported_by INT NOT NULL,
    status ENUM('Open','Claim Pending','Claimed','Resolved') DEFAULT 'Open',
    approval_status ENUM('Pending','Approved','Rejected') DEFAULT 'Pending',
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
    location VARCHAR(150),
    date_reported DATETIME,
    reported_by INT NOT NULL,
    status VARCHAR(30),
    approval_status VARCHAR(20),
    verification_question VARCHAR(255),
    deleted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (reported_by) REFERENCES users(id)
);

CREATE TABLE claim_requests (
    id INT AUTO_INCREMENT PRIMARY KEY,
    item_id INT NOT NULL,
    requested_by INT NOT NULL,
    message VARCHAR(255),
    description_proof VARCHAR(500) DEFAULT NULL,
    claim_image MEDIUMBLOB,
    verification_answer VARCHAR(255) DEFAULT NULL,
    status ENUM('Pending','Approved','Rejected') DEFAULT 'Pending',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    resolved_at TIMESTAMP NULL,
    FOREIGN KEY (item_id) REFERENCES items(id) ON DELETE CASCADE,
    FOREIGN KEY (requested_by) REFERENCES users(id)
);

-- Audit trail of administrative changes (who did what and when)
CREATE TABLE audit_trail (
    id INT AUTO_INCREMENT PRIMARY KEY,
    admin_id INT,
    admin_name VARCHAR(150),
    action VARCHAR(80) NOT NULL,
    target VARCHAR(120),
    details VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Default admin account (student_id: 21-00-0001, password: admin123)
INSERT INTO users (full_name, student_id, password, role) VALUES
('Administrator, System, Anonymous', '21-00-0001', 'admin123', 'Admin');

-- Sample user (format: Last Name, Given Name, Middle Name)
INSERT INTO users (full_name, student_id, password, role, course, year_level, section) VALUES
('De Asis, Diana Kyla, Ignacio', '21-01-1155', 'pass123', 'User', 'BS Information Technology', '3rd Year', 'A');

-- Sample items with verification questions on Found items (already approved)
INSERT INTO items (item_name, description, category, type, location, date_reported, reported_by, status, approval_status, verification_question) VALUES
('Black Wallet', 'Leather wallet with school ID inside', 'Accessories', 'Found', 'ASCOT ENTRANCE', '2026-04-01 09:30:00', 1, 'Open', 'Approved', 'What color is the school ID inside?'),
('iPhone 14', 'Rose gold, cracked screen protector', 'Electronics', 'Lost', 'Engineering Building', '2026-04-03 14:15:00', 2, 'Open', 'Approved', NULL),
('Blue Umbrella', 'Folding umbrella, navy blue color', 'Others', 'Found', 'General Education Buildings', '2026-04-05 11:00:00', 1, 'Open', 'Approved', 'What brand is the umbrella?');
