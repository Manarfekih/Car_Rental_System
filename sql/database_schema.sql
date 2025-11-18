-- Car Rental System Database Schema
CREATE DATABASE IF NOT EXISTS car_rental_db;
USE car_rental_db;

-- Users table for authentication
CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('admin', 'employee', 'client') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Clients table
CREATE TABLE clients (
    client_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20),
    license_number VARCHAR(50) UNIQUE NOT NULL,
    address TEXT,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- Cars table
CREATE TABLE cars (
    car_id INT PRIMARY KEY AUTO_INCREMENT,
    brand VARCHAR(50) NOT NULL,
    model VARCHAR(50) NOT NULL,
    year INT NOT NULL,
    category VARCHAR(30) NOT NULL,
    price_per_day DECIMAL(10,2) NOT NULL,
    status ENUM('available', 'rented', 'maintenance') DEFAULT 'available',
    registration_number VARCHAR(20) UNIQUE NOT NULL,
    color VARCHAR(30),
    mileage INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Rentals table
CREATE TABLE rentals (
    rental_id INT PRIMARY KEY AUTO_INCREMENT,
    client_id INT NOT NULL,
    car_id INT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    actual_return_date DATE NULL,
    total_price DECIMAL(10,2) NOT NULL,
    status ENUM('active', 'completed', 'cancelled') DEFAULT 'active',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (client_id) REFERENCES clients(client_id) ON DELETE CASCADE,
    FOREIGN KEY (car_id) REFERENCES cars(car_id) ON DELETE CASCADE
);

-- Insert sample data
INSERT INTO users (username, password, role) VALUES 
('admin', 'admin123', 'admin'),
('employee1', 'emp123', 'employee'),
('client1', 'client123', 'client');

INSERT INTO clients (user_id, name, email, phone, license_number, address) VALUES 
(3, 'John Doe', 'john@email.com', '123-456-7890', 'LIC123456', '123 Main St');

INSERT INTO cars (brand, model, year, category, price_per_day, status, registration_number, color, mileage) VALUES 
('Toyota', 'Camry', 2023, 'Sedan', 45.00, 'available', 'REG001', 'White', 15000),
('Honda', 'CR-V', 2022, 'SUV', 60.00, 'available', 'REG002', 'Black', 20000),
('BMW', 'X5', 2023, 'Luxury', 120.00, 'available', 'REG003', 'Blue', 5000);
