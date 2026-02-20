package com.example.demo.controller;

import com.example.demo.entity.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerTestRestTemplateTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String getBaseUrl() {
        return "http://localhost:" + port + "/students";
    }

    @Test
    public void testCreateStudent() {
        Student student = new Student("Ivan", 20);
        ResponseEntity<Student> response = restTemplate.postForEntity(getBaseUrl(), student, Student.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Ivan");
        assertThat(response.getBody().getAge()).isEqualTo(20);
    }

    @Test
    public void testGetAllStudents() {
        Student student = new Student("Petr", 22);
        restTemplate.postForEntity(getBaseUrl(), student, Student.class);

        ResponseEntity<List<Student>> response = restTemplate.exchange(
                getBaseUrl(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Student>>() {}
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
    }

    @Test
    public void testGetStudentById() {
        Student student = new Student("Sidor", 25);
        ResponseEntity<Student> created = restTemplate.postForEntity(getBaseUrl(), student, Student.class);
        Long id = created.getBody().getId();

        ResponseEntity<Student> response = restTemplate.getForEntity(getBaseUrl() + "/" + id, Student.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Sidor");
    }

    @Test
    public void testGetStudentByIdNotFound() {
        ResponseEntity<Student> response = restTemplate.getForEntity(getBaseUrl() + "/999999", Student.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testUpdateStudent() {
        Student student = new Student("Anna", 19);
        ResponseEntity<Student> created = restTemplate.postForEntity(getBaseUrl(), student, Student.class);
        Long id = created.getBody().getId();

        Student updated = new Student("Anna Updated", 20);
        restTemplate.put(getBaseUrl() + "/" + id, updated);

        ResponseEntity<Student> response = restTemplate.getForEntity(getBaseUrl() + "/" + id, Student.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getName()).isEqualTo("Anna Updated");
        assertThat(response.getBody().getAge()).isEqualTo(20);
    }

    @Test
    public void testDeleteStudent() {
        Student student = new Student("Oleg", 30);
        ResponseEntity<Student> created = restTemplate.postForEntity(getBaseUrl(), student, Student.class);
        Long id = created.getBody().getId();

        restTemplate.delete(getBaseUrl() + "/" + id);

        ResponseEntity<Student> response = restTemplate.getForEntity(getBaseUrl() + "/" + id, Student.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testGetStudentsByAge() {
        Student s1 = new Student("Young1", 15);
        Student s2 = new Student("Young2", 18);
        Student s3 = new Student("Old", 25);
        restTemplate.postForEntity(getBaseUrl(), s1, Student.class);
        restTemplate.postForEntity(getBaseUrl(), s2, Student.class);
        restTemplate.postForEntity(getBaseUrl(), s3, Student.class);

        ResponseEntity<List<Student>> response = restTemplate.exchange(
                getBaseUrl() + "/by-age?min=10&max=20",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Student>>() {}
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(2);
    }
}