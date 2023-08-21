package com.example.educationapp.controller;

import com.example.educationapp.dto.CourseDto;
import com.example.educationapp.entity.CourseStatus;
import com.example.educationapp.service.AuthorCourseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
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
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        List<CourseDto> expectedCourses = new ArrayList<>();

        CourseDto courseDto = new CourseDto();

        courseDto.setCourseName("first");
        courseDto.setId(1L);
        courseDto.setStatus(CourseStatus.TEMPLATE);
        courseDto.setUpdateDate(OffsetDateTime.now(ZoneOffset.UTC));
        courseDto.setCreateDate(OffsetDateTime.now(ZoneOffset.UTC));

        expectedCourses.add(courseDto);

        when(authorCourseService.getAllCoursesForAuthor()).thenReturn(expectedCourses);

        mockMvc.perform(get("/api/v1/author/courses")
                        .content(objectMapper.writeValueAsString(courseDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(expectedCourses.size()));

        verify(authorCourseService, times(1)).getAllCoursesForAuthor();
    }

    @Test
    public void testCreateCourse() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        CourseDto courseDto = new CourseDto();

        courseDto.setCourseName("first");
        courseDto.setId(1L);
        courseDto.setStatus(CourseStatus.TEMPLATE);
        courseDto.setUpdateDate(OffsetDateTime.now(ZoneOffset.UTC));
        courseDto.setCreateDate(OffsetDateTime.now(ZoneOffset.UTC));
        when(authorCourseService.createCourse(courseDto)).thenReturn(courseDto);

        mockMvc.perform(post("/api/v1/author/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(courseDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").exists());

        verify(authorCourseService, times(1)).createCourse(courseDto);
    }

    @Test
    public void testGetCourse() throws Exception {
        CourseDto expectedCourseDto = new CourseDto();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        Long courseId = 1L;
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        expectedCourseDto.setCourseName("first");
        expectedCourseDto.setId(1L);
        expectedCourseDto.setStatus(CourseStatus.TEMPLATE);
        expectedCourseDto.setUpdateDate(OffsetDateTime.now(ZoneOffset.UTC));
        expectedCourseDto.setCreateDate(OffsetDateTime.now(ZoneOffset.UTC));

        when(authorCourseService.getCourse(courseId)).thenReturn(expectedCourseDto);

        mockMvc.perform(get("/api/v1/author/courses/{id}", courseId)
                        .content((objectMapper.writeValueAsString(expectedCourseDto))))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").exists());

        verify(authorCourseService, times(1)).getCourse(courseId);
    }

    @Test
    public void testUpdateCourse() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        Long courseId = 1L;
        CourseDto courseDto = new CourseDto();
        courseDto.setCourseName("first");
        courseDto.setId(1L);
        courseDto.setStatus(CourseStatus.TEMPLATE);
        courseDto.setUpdateDate(OffsetDateTime.now(ZoneOffset.UTC));
        courseDto.setCreateDate(OffsetDateTime.now(ZoneOffset.UTC));

        CourseDto updatedCourseDto = new CourseDto();

        OffsetDateTime updatedCreateDate = OffsetDateTime.now(ZoneOffset.UTC).plus(10, ChronoUnit.SECONDS);

        updatedCourseDto.setCourseName("second");
        updatedCourseDto.setId(1L);
        updatedCourseDto.setStatus(CourseStatus.TEMPLATE);
        updatedCourseDto.setUpdateDate(OffsetDateTime.now(ZoneOffset.UTC));
        updatedCourseDto.setCreateDate(updatedCreateDate);
        when(authorCourseService.updateCourse(courseId, courseDto)).thenReturn(updatedCourseDto);

        mockMvc.perform(put("/api/v1/author/courses/{id}", courseId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(courseDto)))
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