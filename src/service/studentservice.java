package com.example.demo.service;

import com.example.demo.entity.Faculty;
import com.example.demo.entity.Student;
import com.example.demo.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // CRUD
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student addStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student getStudent(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Студент не найден"));
    }

    public Student updateStudent(Long id, Student student) {
        if (!studentRepository.existsById(id)) {
            throw new RuntimeException("Студент не найден");
        }
        student.setId(id);
        return studentRepository.save(student);
    }

    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new RuntimeException("Студент не найден");
        }
        studentRepository.deleteById(id);
    }

    // Фильтрация по возрасту
    public List<Student> findByAgeBetween(int min, int max) {
        return studentRepository.findByAgeBetween(min, max);
    }

    // Получение факультета студента
    @Transactional(readOnly = true)
    public Faculty getFacultyByStudentId(Long studentId) {
        return studentRepository.findById(studentId)
                .map(Student::getFaculty)
                .orElseThrow(() -> new RuntimeException("Студент не найден"));
    }

    // Новые методы (статистика)
    public long getCountOfStudents() {
        return studentRepository.countAllStudents();
    }

    public double getAverageAge() {
        Double avg = studentRepository.getAverageAge();
        return avg == null ? 0.0 : avg;
    }

    public List<Student> getLastFiveStudents() {
        return studentRepository.findLastFiveStudents();
    }
}