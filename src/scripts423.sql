-- scripts423.sql
-- JOIN-запросы для данных школы

-- 1. Получить всех студентов (имя, возраст) вместе с названиями факультетов
SELECT s.name AS student_name, s.age, f.name AS faculty_name
FROM student s
LEFT JOIN faculty f ON s.faculty_id = f.id;

-- 2. Получить только тех студентов, у которых есть аватарки
SELECT s.name AS student_name, s.age
FROM student s
INNER JOIN avatar a ON s.id = a.student_id;