package com.example.educationapp.controller;

import com.example.educationapp.dto.CourseDto;
import com.example.educationapp.service.AuthorCourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class AuthorCourseControllerTest {

    @Mock
    private AuthorCourseService authorCourseService;

    private AuthorCourseController authorCourseController;
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        authorCourseController = new AuthorCourseController(authorCourseService);
        mockMvc = MockMvcBuilders.standaloneSetup(authorCourseController).build();
    }

    @Test
    public void testGetAllCoursesForAuthor() throws Exception {
        List<CourseDto> expectedCourses = new ArrayList<>();
        expectedCourses.add(new CourseDto()); // Add some dummy data

        when(authorCourseService.getAllCoursesForAuthor()).thenReturn(expectedCourses);

        mockMvc.perform(get("/api/v1/author/courses"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(expectedCourses.size()));

        verify(authorCourseService, times(1)).getAllCoursesForAuthor();
    }

    @Test
    public void testCreateCourse() throws Exception {
        CourseDto courseDto = new CourseDto();
        when(authorCourseService.createCourse(courseDto)).thenReturn(courseDto);

        mockMvc.perform(post("/api/v1/author/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").exists());

        verify(authorCourseService, times(1)).createCourse(courseDto);
    }

    @Test
    public void testGetCourse() throws Exception {
        Long courseId = 1L;
        CourseDto expectedCourseDto = new CourseDto();
        when(authorCourseService.getCourse(courseId)).thenReturn(expectedCourseDto);

        mockMvc.perform(get("/api/v1/author/courses/{id}", courseId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").exists());

        verify(authorCourseService, times(1)).getCourse(courseId);
    }

    @Test
    public void testUpdateCourse() throws Exception {
        Long courseId = 1L;
        CourseDto courseDto = new CourseDto();
        CourseDto updatedCourseDto = new CourseDto();
        when(authorCourseService.updateCourse(courseId, courseDto)).thenReturn(updatedCourseDto);

        mockMvc.perform(put("/api/v1/author/courses/{id}", courseId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").exists());

        verify(authorCourseService, times(1)).updateCourse(courseId, courseDto);
    }

    @Test
    public void testDeleteCourse() throws Exception {
        Long courseId = 1L;

        mockMvc.perform(delete("/api/v1/author/courses/{id}", courseId))
                .andExpect(status().isOk());

        verify(authorCourseService, times(1)).deleteCourse(courseId);
    }
}