package com.example.educationapp.controller;

import com.example.educationapp.dto.request.RequestCourseDto;
import com.example.educationapp.dto.response.ResponseCourseDto;
import com.example.educationapp.entity.CourseStatus;
import com.example.educationapp.exception.CourseNameException;
import com.example.educationapp.exception.InvalidStatusException;
import com.example.educationapp.exception.UserNotFoundException;
import com.example.educationapp.repo.UserRepo;
import com.example.educationapp.service.AuthorCourseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@WithMockUser(username = "Lipsar", authorities = "AUTHOR")
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthorCourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorCourseService authorCourseService;

    @Test
    public void testGetAllCoursesForAuthor() throws Exception {
        List<ResponseCourseDto> courses = new ArrayList<>();
        ResponseCourseDto responseCourseDto = new ResponseCourseDto();
        courses.add(responseCourseDto);
        when(authorCourseService.getAllCoursesForAuthor()).thenReturn(courses);

        mockMvc.perform(get("/api/v1/author/courses")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(courses.size())));
    }

    @Test
    public void testCreateCourse() throws Exception {
        RequestCourseDto requestCourseDto = new RequestCourseDto();
        requestCourseDto.setCourseName("Test Course");
        requestCourseDto.setCourseStatus(CourseStatus.TEMPLATE);


        ResponseCourseDto responseCourseDto = new ResponseCourseDto();
        responseCourseDto.setId(1L);
        responseCourseDto.setCourseName(requestCourseDto.getCourseName());
        responseCourseDto.setCourseStatus(requestCourseDto.getCourseStatus());

        when(authorCourseService.createCourse(requestCourseDto)).thenReturn(responseCourseDto);

        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(post("/api/v1/author/courses").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestCourseDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.courseName").value("Test Course"));
    }

    @Test
    public void testGetCourse() throws Exception {
        Long courseId = 1L; // replace with actual ID
        ResponseCourseDto responseCourseDto = new ResponseCourseDto(); // populate with test data
        when(authorCourseService.getCourse(courseId)).thenReturn(responseCourseDto);

        mockMvc.perform(get("/api/v1/author/courses/{id}", courseId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(responseCourseDto.getId()))
                .andExpect(jsonPath("$.courseName").value(responseCourseDto.getCourseName()))
                .andExpect(jsonPath("$.courseStatus").value(responseCourseDto.getCourseStatus()));
    }

    @Test
    public void testUpdateCourse() throws Exception {
        Long courseId = 1L;
        RequestCourseDto requestCourseDto = new RequestCourseDto();
        ResponseCourseDto responseCourseDto = new ResponseCourseDto();
        when(authorCourseService.updateCourse(courseId, requestCourseDto)).thenReturn(responseCourseDto);

        mockMvc.perform(put("/api/v1/author/courses/{id}", courseId).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestCourseDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(responseCourseDto.getId()))
                .andExpect(jsonPath("$.courseName").value(responseCourseDto.getCourseName()))
                .andExpect(jsonPath("$.courseStatus").value(responseCourseDto.getCourseStatus()));
    }

    @Test
    public void testDeleteCourse() throws Exception {
        Long courseId = 1L;

        mockMvc.perform(delete("/api/v1/author/courses/{id}", courseId).with(csrf()))
                .andExpect(status().isOk());

        verify(authorCourseService).deleteCourse(courseId);
    }

    @Test
    public void testCreateCourseCourseNameException() throws Exception {
        RequestCourseDto requestCourseDto = new RequestCourseDto();
        requestCourseDto.setCourseName("Test Course");

        ObjectMapper objectMapper = new ObjectMapper();

        when(authorCourseService.createCourse(requestCourseDto))
                .thenThrow(new CourseNameException("Course name is already exists"));

        mockMvc.perform(post("/api/v1/author/courses").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestCourseDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Course name is already exists"));
    }

    @Test
    public void testCreateCourseInvalidStatusException() throws Exception {
        RequestCourseDto requestCourseDto = new RequestCourseDto();
        requestCourseDto.setCourseName("Test Course");
        requestCourseDto.setCourseStatus(CourseStatus.ONGOING);

        ObjectMapper objectMapper = new ObjectMapper();

        when(authorCourseService.createCourse(requestCourseDto))
                .thenThrow(new InvalidStatusException("Course can only be created with Template status."));

        mockMvc.perform(post("/api/v1/author/courses").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestCourseDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Course can only be created with Template status."));
    }

    @Test
    public void testDeleteCourseUserNotFoundException() throws Exception {
        Long courseId = 1L;

        doThrow(new UserNotFoundException("User not found"))
                .when(authorCourseService).deleteCourse(courseId);

        mockMvc.perform(delete("/api/v1/author/courses/{id}", courseId).with(csrf()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User not found"));
    }

    @Test
    @WithMockUser(username = "studentTest", authorities = "STUDENT")
    public void testGetCourseAuthoritiesCheckStudent() throws Exception {
        Long courseId = 1L;
        ResponseCourseDto responseCourseDto = new ResponseCourseDto();
        when(authorCourseService.getCourse(courseId)).thenReturn(responseCourseDto);

        mockMvc.perform(get("/api/v1/author/courses/{id}", courseId))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "teacherTest", authorities = "TEACHER")
    public void testGetCourseAuthoritiesCheckTeacher() throws Exception {
        Long courseId = 1L;
        ResponseCourseDto responseCourseDto = new ResponseCourseDto();
        when(authorCourseService.getCourse(courseId)).thenReturn(responseCourseDto);

        mockMvc.perform(get("/api/v1/author/courses/{id}", courseId))
                .andExpect(status().isForbidden());
    }
}