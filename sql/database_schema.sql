-- Car Rental System Database Schema
CREATE DATABASE IF NOT EXISTS car_rental_db;
USE car_rental_db;

-- USERS TABLE (Authentication + Roles)
CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('admin', 'employee', 'client') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- CLIENTS TABLE (Information About Clients)
CREATE TABLE clients (
    client_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    license_number VARCHAR(50) UNIQUE NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- CARS TABLE
CREATE TABLE cars (
    car_id INT PRIMARY KEY AUTO_INCREMENT,
    brand VARCHAR(50) NOT NULL,
    model VARCHAR(50) NOT NULL,
    year INT NOT NULL,
    price_per_day DECIMAL(10,2) NOT NULL,
    status ENUM('available', 'rented') DEFAULT 'available',
    registration_number VARCHAR(30) UNIQUE NOT NULL
);

-- RENTALS TABLE
CREATE TABLE rentals (
    rental_id INT PRIMARY KEY AUTO_INCREMENT,
    client_id INT NOT NULL,
    car_id INT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    return_date DATE DEFAULT NULL,
    total_price DECIMAL(10,2),
    status ENUM('active', 'returned') DEFAULT 'active',
    FOREIGN KEY (client_id) REFERENCES clients(client_id) ON DELETE CASCADE,
    FOREIGN KEY (car_id) REFERENCES cars(car_id) ON DELETE CASCADE
);

-- SAMPLE DATA
INSERT INTO users (username, password, role) VALUES
('Manar', 'admin123', 'admin'),
('employee1', 'emp123', 'employee'),
('client1', 'client123', 'client');

INSERT INTO clients (user_id, full_name, phone, license_number) VALUES
(3, 'John Doe', '123456789', 'LIC998877');

INSERT INTO cars (brand, model, year, price_per_day, status, registration_number) VALUES
('Toyota', 'Corolla', 2020, 40, 'available', 'TN-101'),
('BMW', 'X5', 2022, 150, 'available', 'TN-202'),
('Honda', 'Civic', 2019, 35, 'available', 'TN-303');