package com.example.demo.service;

import com.example.demo.entity.Faculty;
import com.example.demo.entity.Student;
import com.example.demo.repository.StudentRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // Стандартные методы (примеры)
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student addStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student getStudent(Long id) {
        return studentRepository.findById(id).orElseThrow(() -> new RuntimeException("Студент не найден"));
    }

    public Student updateStudent(Long id, Student student) {
        student.setId(id);
        return studentRepository.save(student);
    }

    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    // Новый метод для поиска по возрасту
    public List<Student> findByAgeBetween(int min, int max) {
        return studentRepository.findByAgeBetween(min, max);
    }

    // Метод для получения факультета студента
    public Faculty getFacultyByStudentId(Long studentId) {
        return studentRepository.findById(studentId)
                .map(Student::getFaculty)
                .orElseThrow(() -> new RuntimeException("Студент не найден"));
    }
}