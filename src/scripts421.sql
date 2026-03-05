-- scripts421.sql
-- Добавление ограничений для таблиц Student и Faculty

-- 1. Возраст студента не может быть меньше 16 лет
ALTER TABLE student
ADD CONSTRAINT age_check CHECK (age >= 16);

-- 2. Имена студентов должны быть уникальны и не NULL
-- Если поле уже допускает NULL, сначала сделаем его NOT NULL
UPDATE student SET name = 'Unknown' WHERE name IS NULL;
ALTER TABLE student
ALTER COLUMN name SET NOT NULL,
ADD CONSTRAINT name_unique UNIQUE (name);

-- 3. Пара (название факультета, цвет) должна быть уникальной
ALTER TABLE faculty
ADD CONSTRAINT unique_name_color UNIQUE (name, color);

-- 4. При создании студента без возраста присваивать 20 лет
-- Это можно сделать через DEFAULT, но DEFAULT сработает только если значение не указано явно.
-- Если в INSERT не указан age, то подставится 20.
ALTER TABLE student
ALTER COLUMN age SET DEFAULT 20;