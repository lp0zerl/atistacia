package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;
import java.util.Optional;

@Service
public class StudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student createStudent(Student student) {
        logger.info("Was invoked method for create student");
        return studentRepository.save(student);
    }

    public Student findStudent(long id) {
        logger.info("Was invoked method for find student by id = {}", id);
        Optional<Student> optional = studentRepository.findById(id);
        if (optional.isEmpty()) {
            logger.warn("No student with id = {}", id);
            return null;
        }
        return optional.get();
    }

    public Student editStudent(Student student) {
        logger.info("Was invoked method for edit student with id = {}", student.getId());
        if (!studentRepository.existsById(student.getId())) {
            logger.error("Cannot edit student: no student with id = {}", student.getId());
            // Здесь можно выбросить исключение, но пока просто возвращаем null
            return null;
        }
        return studentRepository.save(student);
    }

    public void deleteStudent(long id) {
        logger.info("Was invoked method for delete student with id = {}", id);
        if (!studentRepository.existsById(id)) {
            logger.warn("Attempt to delete non-existent student with id = {}", id);
        }
        studentRepository.deleteById(id);
    }

    public Collection<Student> getAllStudents() {
        logger.info("Was invoked method for get all students");
        return studentRepository.findAll();
    }
}