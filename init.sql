-- Smart Movie Booking System Database Initialization Script

-- Create database
CREATE DATABASE IF NOT EXISTS smartmoviebooking;
USE smartmoviebooking;

-- Drop tables if they exist (for clean setup)
DROP TABLE IF EXISTS bookings;
DROP TABLE IF EXISTS theatres;
DROP TABLE IF EXISTS movies;
DROP TABLE IF EXISTS users;

-- Create users table
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Create movies table
CREATE TABLE movies (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    genre VARCHAR(50),
    rating DECIMAL(3,1),
    duration INT,
    language VARCHAR(50),
    description TEXT,
    poster_url VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_title (title)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Create theatres table
CREATE TABLE theatres (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    address VARCHAR(255) NOT NULL,
    latitude DOUBLE NOT NULL,
    longitude DOUBLE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_location (latitude, longitude)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Create bookings table
CREATE TABLE bookings (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    movie_id INT NOT NULL,
    theatre_id INT NOT NULL,
    seats VARCHAR(255) NOT NULL,
    total_price DECIMAL(10,2) NOT NULL,
    status VARCHAR(20) DEFAULT 'CONFIRMED',
    booked_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (movie_id) REFERENCES movies(id) ON DELETE CASCADE,
    FOREIGN KEY (theatre_id) REFERENCES theatres(id) ON DELETE CASCADE,
    INDEX idx_user (user_id),
    INDEX idx_movie_theatre (movie_id, theatre_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Insert sample theatre data
INSERT INTO theatres (name, address, latitude, longitude) VALUES
('PVR Cinemas Phoenix', 'Phoenix Marketcity, Mumbai', 19.0896, 72.8869),
('INOX Megaplex', 'Inorbit Mall, Mumbai', 19.1758, 72.9476),
('Cinepolis Fun Republic', 'Andheri West, Mumbai', 19.1368, 72.8261),
('Carnival Cinemas', 'Wadala, Mumbai', 19.0176, 72.8561),
('MovieMax Multiplex', 'Thane West, Mumbai', 19.2183, 72.9781);

-- Insert demo user (password: password123 - in production, this should be hashed)
-- For demo purposes, using plain text. In production, use BCrypt or similar hashing
INSERT INTO users (name, email, password) VALUES
('Demo User', 'demo@email.com', 'password123');

-- Insert sample movies for testing (optional - will be populated from API)
INSERT INTO movies (title, genre, rating, duration, language, description, poster_url) VALUES
('The Shawshank Redemption', 'Drama', 9.3, 142, 'English', 'Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency.', 'https://image.tmdb.org/t/p/w500/q6y0Go1tsGEsmtFryDOJo3dEmqu.jpg'),
('The Godfather', 'Crime', 9.2, 175, 'English', 'The aging patriarch of an organized crime dynasty transfers control of his clandestine empire to his reluctant son.', 'https://image.tmdb.org/t/p/w500/3bhkrj58Vtu7enYsRolD1fZdja1.jpg'),
('The Dark Knight', 'Action', 9.0, 152, 'English', 'When the menace known as the Joker wreaks havoc and chaos on the people of Gotham, Batman must accept one of the greatest psychological and physical tests.', 'https://image.tmdb.org/t/p/w500/qJ2tW6WMUDux911r6m7haRef0WH.jpg');

-- Display success message
SELECT 'Database initialized successfully!' AS Status;
