package ru.hogwarts.school.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        Student createdStudent = studentService.createStudent(student);
        return ResponseEntity.ok(createdStudent);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudent(@PathVariable long id) {
        Student student = studentService.findStudent(id);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @PutMapping
    public ResponseEntity<Student> editStudent(@RequestBody Student student) {
        Student foundStudent = studentService.findStudent(student.getId());
        if (foundStudent == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        Student editedStudent = studentService.editStudent(student);
        return ResponseEntity.ok(editedStudent);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Collection<Student>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    // Эндпоинты из урока 4.5
    @GetMapping("/names-starting-with-a")
    public ResponseEntity<List<String>> getStudentsNamesStartingWithA() {
        return ResponseEntity.ok(studentService.getStudentsNamesStartingWithA());
    }

    @GetMapping("/average-age")
    public ResponseEntity<Double> getAverageAge() {
        return ResponseEntity.ok(studentService.getAverageAge());
    }

    // ====================== УРОК 4.6 ======================

    /**
     * Выводит имена первых шести студентов в консоль:
     * - 1-2 в основном потоке,
     * - 3-4 в отдельном потоке,
     * - 5-6 в ещё одном отдельном потоке.
     * Используется несинхронизированный вывод (возможно перемешивание).
     */
    @GetMapping("/print-parallel")
    public void printParallel() {
        List<Student> students = studentService.getAllStudents().stream().limit(6).toList();

        // Первые два в основном потоке
        if (students.size() > 0) System.out.println(students.get(0).getName());
        if (students.size() > 1) System.out.println(students.get(1).getName());

        // Поток для 3-4
        if (students.size() > 2) {
            new Thread(() -> {
                if (students.size() > 2) System.out.println(students.get(2).getName());
                if (students.size() > 3) System.out.println(students.get(3).getName());
            }).start();
        }

        // Поток для 5-6
        if (students.size() > 4) {
            new Thread(() -> {
                if (students.size() > 4) System.out.println(students.get(4).getName());
                if (students.size() > 5) System.out.println(students.get(5).getName());
            }).start();
        }
    }

    /**
     * Выводит имена первых шести студентов в консоль с использованием синхронизированного метода.
     * Распределение по потокам аналогично printParallel.
     */
    @GetMapping("/print-synchronized")
    public void printSynchronized() {
        List<Student> students = studentService.getAllStudents().stream().limit(6).toList();

        // Первые два в основном потоке
        if (students.size() > 0) printName(students.get(0).getName());
        if (students.size() > 1) printName(students.get(1).getName());

        // Поток для 3-4
        if (students.size() > 2) {
            new Thread(() -> {
                if (students.size() > 2) printName(students.get(2).getName());
                if (students.size() > 3) printName(students.get(3).getName());
            }).start();
        }

        // Поток для 5-6
        if (students.size() > 4) {
            new Thread(() -> {
                if (students.size() > 4) printName(students.get(4).getName());
                if (students.size() > 5) printName(students.get(5).getName());
            }).start();
        }
    }

    /**
     * Синхронизированный метод для вывода имени в консоль.
     */
    private synchronized void printName(String name) {
        System.out.println(name);
    }
}