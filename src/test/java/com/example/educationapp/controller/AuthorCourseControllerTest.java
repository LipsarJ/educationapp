package com.example.educationapp.controller;

import com.example.educationapp.dto.request.RequestCourseDto;
import com.example.educationapp.dto.response.ResponseCourseDto;
import com.example.educationapp.entity.CourseStatus;
import com.example.educationapp.entity.User;
import com.example.educationapp.security.service.UserContext;
import com.example.educationapp.service.AuthorCourseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AuthorCourseController.class)
@WithMockUser(username = "Lipsar", roles = "AUTHOR")
@AutoConfigureMockMvc
public class AuthorCourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorCourseService authorCourseService;

    @MockBean
    private UserContext userContext;

    @Test
    public void testGetAllCoursesForAuthor() throws Exception {
        List<ResponseCourseDto> courses = new ArrayList<>(); // populate with test data
        ResponseCourseDto responseCourseDto = new ResponseCourseDto();
        courses.add(responseCourseDto);
        when(authorCourseService.getAllCoursesForAuthor()).thenReturn(courses);

        mockMvc.perform(get("/api/v1/author/courses"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$", hasSize(courses.size())));
    }

    @Test
    @WithMockUser(username = "Lipsar", roles = "AUTHOR")
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

        mockMvc.perform(post("/api/v1/author/courses")
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
        Long courseId = 1L; // replace with actual ID
        RequestCourseDto requestCourseDto = new RequestCourseDto();
        ResponseCourseDto responseCourseDto = new ResponseCourseDto();
        when(authorCourseService.updateCourse(courseId, requestCourseDto)).thenReturn(responseCourseDto);

        mockMvc.perform(put("/api/v1/author/courses/{id}", courseId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestCourseDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(responseCourseDto.getId()))
                .andExpect(jsonPath("$.courseName").value(responseCourseDto.getCourseName()))
                .andExpect(jsonPath("$.courseStatus").value(responseCourseDto.getCourseStatus()));
    }

    @Test
    @WithMockUser(username = "Lipsar", roles = "AUTHOR")
    public void testDeleteCourse() throws Exception {
        Long courseId = 1L; // replace with actual ID

        mockMvc.perform(delete("/api/v1/author/courses/{id}", courseId))
                .andExpect(status().isOk());

        verify(authorCourseService).deleteCourse(courseId);
    }
}