package com.example.demo.controller;

import com.example.demo.entity.Faculty;
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
public class FacultyControllerTestRestTemplateTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String getBaseUrl() {
        return "http://localhost:" + port + "/faculties";
    }

    @Test
    public void testCreateFaculty() {
        Faculty faculty = new Faculty("Gryffindor", "Red");
        ResponseEntity<Faculty> response = restTemplate.postForEntity(getBaseUrl(), faculty, Faculty.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Gryffindor");
        assertThat(response.getBody().getColor()).isEqualTo("Red");
    }

    @Test
    public void testGetAllFaculties() {
        Faculty faculty = new Faculty("Slytherin", "Green");
        restTemplate.postForEntity(getBaseUrl(), faculty, Faculty.class);

        ResponseEntity<List<Faculty>> response = restTemplate.exchange(
                getBaseUrl(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Faculty>>() {}
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
    }

    @Test
    public void testGetFacultyById() {
        Faculty faculty = new Faculty("Ravenclaw", "Blue");
        ResponseEntity<Faculty> created = restTemplate.postForEntity(getBaseUrl(), faculty, Faculty.class);
        Long id = created.getBody().getId();

        ResponseEntity<Faculty> response = restTemplate.getForEntity(getBaseUrl() + "/" + id, Faculty.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Ravenclaw");
    }

    @Test
    public void testGetFacultyByIdNotFound() {
        ResponseEntity<Faculty> response = restTemplate.getForEntity(getBaseUrl() + "/999999", Faculty.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testUpdateFaculty() {
        Faculty faculty = new Faculty("Hufflepuff", "Yellow");
        ResponseEntity<Faculty> created = restTemplate.postForEntity(getBaseUrl(), faculty, Faculty.class);
        Long id = created.getBody().getId();

        Faculty updated = new Faculty("Hufflepuff Updated", "Gold");
        restTemplate.put(getBaseUrl() + "/" + id, updated);

        ResponseEntity<Faculty> response = restTemplate.getForEntity(getBaseUrl() + "/" + id, Faculty.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getName()).isEqualTo("Hufflepuff Updated");
        assertThat(response.getBody().getColor()).isEqualTo("Gold");
    }

    @Test
    public void testDeleteFaculty() {
        Faculty faculty = new Faculty("Durmstrang", "Brown");
        ResponseEntity<Faculty> created = restTemplate.postForEntity(getBaseUrl(), faculty, Faculty.class);
        Long id = created.getBody().getId();

        restTemplate.delete(getBaseUrl() + "/" + id);

        ResponseEntity<Faculty> response = restTemplate.getForEntity(getBaseUrl() + "/" + id, Faculty.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testSearchFaculties() {
        Faculty f1 = new Faculty("Beauxbatons", "Blue");
        Faculty f2 = new Faculty("Castelobruxo", "Green");
        restTemplate.postForEntity(getBaseUrl(), f1, Faculty.class);
        restTemplate.postForEntity(getBaseUrl(), f2, Faculty.class);

        ResponseEntity<List<Faculty>> response = restTemplate.exchange(
                getBaseUrl() + "/search?query=blue",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Faculty>>() {}
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
        assertThat(response.getBody().get(0).getName()).isEqualTo("Beauxbatons");
    }
}