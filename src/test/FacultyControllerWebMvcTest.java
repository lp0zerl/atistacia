package com.example.demo.controller;

import com.example.demo.entity.Faculty;
import com.example.demo.service.FacultyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FacultyController.class)
public class FacultyControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FacultyService facultyService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateFaculty() throws Exception {
        Faculty faculty = new Faculty("Gryffindor", "Red");
        Faculty saved = new Faculty(1L, "Gryffindor", "Red");
        when(facultyService.addFaculty(any(Faculty.class))).thenReturn(saved);

        mockMvc.perform(post("/faculties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(faculty)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Gryffindor"))
                .andExpect(jsonPath("$.color").value("Red"));
    }

    @Test
    public void testGetAllFaculties() throws Exception {
        List<Faculty> faculties = Arrays.asList(
                new Faculty(1L, "Slytherin", "Green"),
                new Faculty(2L, "Ravenclaw", "Blue")
        );
        when(facultyService.getAllFaculties()).thenReturn(faculties);

        mockMvc.perform(get("/faculties"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Slytherin"))
                .andExpect(jsonPath("$[1].color").value("Blue"));
    }

    @Test
    public void testGetFacultyById() throws Exception {
        Faculty faculty = new Faculty(1L, "Hufflepuff", "Yellow");
        when(facultyService.getFaculty(1L)).thenReturn(faculty);

        mockMvc.perform(get("/faculties/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Hufflepuff"));
    }

    @Test
    public void testGetFacultyByIdNotFound() throws Exception {
        when(facultyService.getFaculty(999L)).thenThrow(new RuntimeException("Faculty not found"));

        mockMvc.perform(get("/faculties/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateFaculty() throws Exception {
        Faculty updated = new Faculty(1L, "Hufflepuff Updated", "Gold");
        when(facultyService.updateFaculty(eq(1L), any(Faculty.class))).thenReturn(updated);

        mockMvc.perform(put("/faculties/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Hufflepuff Updated"));
    }

    @Test
    public void testDeleteFaculty() throws Exception {
        doNothing().when(facultyService).deleteFaculty(1L);

        mockMvc.perform(delete("/faculties/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void testSearchFaculties() throws Exception {
        List<Faculty> faculties = Arrays.asList(
                new Faculty(1L, "Beauxbatons", "Blue")
        );
        when(facultyService.findByNameOrColor("blue")).thenReturn(faculties);

        mockMvc.perform(get("/faculties/search")
                        .param("query", "blue"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Beauxbatons"));
    }
}