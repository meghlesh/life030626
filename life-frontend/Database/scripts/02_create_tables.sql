-- =============================================
-- CWS.life — Create All Tables
-- File: 02_create_tables.sql
-- Run this after 01_create_database.sql
-- =============================================

USE cwslife_db;

-- Admin and Users table
CREATE TABLE IF NOT EXISTS users (
  id         INT AUTO_INCREMENT PRIMARY KEY,
  full_name  VARCHAR(100)        NOT NULL,
  email      VARCHAR(150) UNIQUE NOT NULL,
  password   VARCHAR(255)        NOT NULL,
  phone      VARCHAR(20),
  role       VARCHAR(50) DEFAULT 'USER',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Password reset tokens store table
CREATE TABLE password_reset_tokens (
  id         INT AUTO_INCREMENT PRIMARY KEY,
  email      VARCHAR(150) NOT NULL,
  token      VARCHAR(255) NOT NULL,
  expires_at TIMESTAMP   NOT NULL,
  is_used    BOOLEAN DEFAULT FALSE,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
-- Contact form submissions
CREATE TABLE IF NOT EXISTS contact_inquiries (
  id         INT AUTO_INCREMENT PRIMARY KEY,
  first_name VARCHAR(100),
  last_name  VARCHAR(100),
  email      VARCHAR(150),
  phone      VARCHAR(20),
  message    TEXT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Upcoming Event table
CREATE TABLE events (
  id          INT AUTO_INCREMENT PRIMARY KEY,
  title       VARCHAR(200) NOT NULL,
  category    VARCHAR(100),
  event_date  DATE,
  description TEXT,
  image_url   VARCHAR(500),
  status      VARCHAR(50) DEFAULT 'UPCOMING',
  created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);