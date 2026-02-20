package com.example.demo.service;

import com.example.demo.entity.Faculty;
import com.example.demo.entity.Student;
import com.example.demo.repository.FacultyRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FacultyService {
    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    // Стандартные методы
    public List<Faculty> getAllFaculties() {
        return facultyRepository.findAll();
    }

    public Faculty addFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public Faculty getFaculty(Long id) {
        return facultyRepository.findById(id).orElseThrow(() -> new RuntimeException("Факультет не найден"));
    }

    public Faculty updateFaculty(Long id, Faculty faculty) {
        faculty.setId(id);
        return facultyRepository.save(faculty);
    }

    public void deleteFaculty(Long id) {
        facultyRepository.deleteById(id);
    }

    // Новый метод поиска по имени/цвету
    public List<Faculty> findByNameOrColor(String query) {
        return facultyRepository.findByNameContainingIgnoreCaseOrColorContainingIgnoreCase(query, query);
    }

    // Метод для получения студентов факультета
    public List<Student> getStudentsByFacultyId(Long facultyId) {
        return facultyRepository.findById(facultyId)
                .map(Faculty::getStudents)
                .orElseThrow(() -> new RuntimeException("Факультет не найден"));
    }
}