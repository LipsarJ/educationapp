package com.example.educationapp.controller;

import com.example.educationapp.dto.request.RequestCourseDto;
import com.example.educationapp.dto.response.ResponseCourseDto;
import com.example.educationapp.entity.CourseStatus;
import com.example.educationapp.service.AuthorCourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthorCourseController.class)
public class AuthorCourseControllerTest {

    @MockBean
    private AuthorCourseService authorCourseService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(new AuthorCourseController(authorCourseService)).build();
    }

    @Test
    public void testGetAllCoursesForAuthor() throws Exception {
        List<ResponseCourseDto> courses = new ArrayList<>();
        ResponseCourseDto responseCourseDto1 = new ResponseCourseDto();
        responseCourseDto1.setCourseName("first");
        responseCourseDto1.setCourseStatus(CourseStatus.TEMPLATE);

        ResponseCourseDto responseCourseDto2 = new ResponseCourseDto();
        responseCourseDto2.setCourseName("second");
        responseCourseDto2.setCourseStatus(CourseStatus.TEMPLATE);

        courses.add(responseCourseDto1);
        courses.add(responseCourseDto2);

        when(authorCourseService.getAllCoursesForAuthor()).thenReturn(courses);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/author/courses"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(courses.size()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].courseName").value(responseCourseDto1.getCourseName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].courseStatus").value(responseCourseDto1.getCourseStatus().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].courseName").value(responseCourseDto2.getCourseName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].courseStatus").value(responseCourseDto2.getCourseStatus().toString()));
    }

    @Test
    public void testCreateCourse() throws Exception {
        ResponseCourseDto responseCourseDto = new ResponseCourseDto();
        responseCourseDto.setCourseName("first");
        responseCourseDto.setCourseStatus(CourseStatus.TEMPLATE);

        when(authorCourseService.createCourse(any(RequestCourseDto.class))).thenReturn(responseCourseDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/author/courses")
                        .content("{\"courseName\":\"first\",\"courseStatus\":\"TEMPLATE\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.courseName").value(responseCourseDto.getCourseName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.courseStatus").value(responseCourseDto.getCourseStatus().toString()));
    }

    @Test
    public void testGetCourse() throws Exception {
        ResponseCourseDto responseCourseDto = new ResponseCourseDto();
        responseCourseDto.setCourseName("first");
        responseCourseDto.setCourseStatus(CourseStatus.TEMPLATE);

        when(authorCourseService.getCourse(1L)).thenReturn(responseCourseDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/author/courses/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.courseName").value(responseCourseDto.getCourseName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.courseStatus").value(responseCourseDto.getCourseStatus().toString()));
    }

    @Test
    public void testUpdateCourse() throws Exception {
        ResponseCourseDto responseCourseDto = new ResponseCourseDto();
        responseCourseDto.setCourseName("first");
        responseCourseDto.setCourseStatus(CourseStatus.TEMPLATE);

        when(authorCourseService.updateCourse(anyLong(), any(RequestCourseDto.class))).thenReturn(responseCourseDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/author/courses/1")
                        .content("{\"courseName\":\"first\",\"courseStatus\":\"TEMPLATE\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.courseName").value(responseCourseDto.getCourseName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.courseStatus").value(responseCourseDto.getCourseStatus().toString()));
    }

    @Test
    public void testDeleteCourse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/author/courses/1"))
                .andExpect(status().isOk());
    }
}