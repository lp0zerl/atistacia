-- scripts422.sql
-- Создание таблиц для описания: у каждого человека есть машина, несколько человек могут пользоваться одной машиной

-- Таблица машин
CREATE TABLE IF NOT EXISTS car (
    id BIGSERIAL PRIMARY KEY,
    brand VARCHAR(100) NOT NULL,
    model VARCHAR(100) NOT NULL,
    price DECIMAL(10,2) NOT NULL CHECK (price >= 0)
);

-- Таблица людей
CREATE TABLE IF NOT EXISTS person (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    age INT NOT NULL CHECK (age >= 0),
    has_driving_license BOOLEAN NOT NULL DEFAULT FALSE,
    car_id BIGINT REFERENCES car(id) ON DELETE SET NULL
);