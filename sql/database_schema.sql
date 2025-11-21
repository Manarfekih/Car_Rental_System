-- Create the database
CREATE DATABASE IF NOT EXISTS car_rental_db;
USE car_rental_db;

-- USERS TABLE (matches User class)
CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE, 
    password VARCHAR(255) NOT NULL,
    role ENUM('admin', 'employee', 'client') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- CLIENTS TABLE (matches Client class)
CREATE TABLE clients (
    client_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL, -- links to User
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE,
    phone VARCHAR(20),
    license_number VARCHAR(50) UNIQUE NOT NULL,
    address TEXT,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- CARS TABLE (matches Car class)
CREATE TABLE cars (
    car_id INT PRIMARY KEY AUTO_INCREMENT,
    brand VARCHAR(50) NOT NULL,
    model VARCHAR(50) NOT NULL,
    year INT NOT NULL,
    category VARCHAR(30), -- optional: Sedan, SUV, Luxury
    price_per_day DECIMAL(10,2) NOT NULL,
    status ENUM('available', 'rented', 'maintenance') DEFAULT 'available'
);

-- RENTALS TABLE (matches Rental class)
CREATE TABLE rentals (
    rental_id INT PRIMARY KEY AUTO_INCREMENT,
    client_id INT NOT NULL,
    car_id INT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    total_price DECIMAL(10,2) NOT NULL DEFAULT 0,
    status ENUM('active', 'completed', 'cancelled') DEFAULT 'active',
    FOREIGN KEY (client_id) REFERENCES clients(client_id) ON DELETE CASCADE,
    FOREIGN KEY (car_id) REFERENCES cars(car_id) ON DELETE CASCADE
);

-- SAMPLE DATA
INSERT INTO users (username, email, password, role) VALUES
('Manar', 'admin@example.com', 'admin123', 'admin'),
('employee1', 'employee1@example.com', 'emp123', 'employee'),
('client1', 'client1@example.com', 'client123', 'client');

INSERT INTO clients (user_id, name, email, phone, license_number, address) VALUES
(3, 'John Doe', 'john@email.com', '123456789', 'LIC998877', '123 Main St');

INSERT INTO cars (brand, model, year, category, price_per_day, status) VALUES
('Toyota', 'Corolla', 2020, 'Sedan', 40, 'available'),
('BMW', 'X5', 2022, 'Luxury', 150, 'available'),
('Honda', 'Civic', 2019, 'Sedan', 35, 'available');
