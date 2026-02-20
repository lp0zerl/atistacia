package com.example.demo.controller;

import com.example.demo.entity.Faculty;
import com.example.demo.entity.Student;
import com.example.demo.service.FacultyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/faculties")
public class FacultyController {
    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @GetMapping
    public ResponseEntity<List<Faculty>> getAllFaculties() {
        return ResponseEntity.ok(facultyService.getAllFaculties());
    }

    @PostMapping
    public ResponseEntity<Faculty> createFaculty(@RequestBody Faculty faculty) {
        return ResponseEntity.ok(facultyService.addFaculty(faculty));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Faculty> getFaculty(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(facultyService.getFaculty(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Faculty> updateFaculty(@PathVariable Long id, @RequestBody Faculty faculty) {
        try {
            return ResponseEntity.ok(facultyService.updateFaculty(id, faculty));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFaculty(@PathVariable Long id) {
        try {
            facultyService.deleteFaculty(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Faculty>> searchFaculties(@RequestParam String query) {
        return ResponseEntity.ok(facultyService.findByNameOrColor(query));
    }

    @GetMapping("/{id}/students")
    public ResponseEntity<List<Student>> getStudentsByFaculty(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(facultyService.getStudentsByFacultyId(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}