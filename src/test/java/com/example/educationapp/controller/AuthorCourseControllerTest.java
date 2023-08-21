package com.example.educationapp.controller;

import com.example.educationapp.dto.CourseDto;
import com.example.educationapp.entity.CourseStatus;
import com.example.educationapp.service.AuthorCourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthorCourseController.class)
public class AuthorCourseControllerTest {

    @MockBean
    private AuthorCourseService authorCourseService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(new AuthorCourseController(authorCourseService)).build();
    }

    @Test
    public void testGetAllCoursesForAuthor() throws Exception {
        List<CourseDto> courses = new ArrayList<>();
        CourseDto courseDto1 = new CourseDto();
        courseDto1.setCourseName("first");
        courseDto1.setStatus(CourseStatus.TEMPLATE);
        courseDto1.setUpdateDate(OffsetDateTime.now(ZoneOffset.UTC));
        courseDto1.setCreateDate(OffsetDateTime.now(ZoneOffset.UTC));

        CourseDto courseDto2 = new CourseDto();
        courseDto2.setCourseName("second");
        courseDto2.setStatus(CourseStatus.TEMPLATE);
        courseDto2.setUpdateDate(OffsetDateTime.now(ZoneOffset.UTC));
        courseDto2.setCreateDate(OffsetDateTime.now(ZoneOffset.UTC));

        courses.add(courseDto1);
        courses.add(courseDto2);

        when(authorCourseService.getAllCoursesForAuthor()).thenReturn(courses);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/author/courses"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(courses.size()));
    }

    @Test
    public void testCreateCourse() throws Exception {
        CourseDto courseDto = new CourseDto();
        courseDto.setCourseName("first");
        courseDto.setStatus(CourseStatus.TEMPLATE);
        courseDto.setUpdateDate(OffsetDateTime.now(ZoneOffset.UTC));
        courseDto.setCreateDate(OffsetDateTime.now(ZoneOffset.UTC));

        when(authorCourseService.createCourse(any(CourseDto.class))).thenReturn(courseDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/author/courses")
                        .content("{\"courseName\":\"first\",\"status\":\"TEMPLATE\"," +
                                "\"updateDate\":\"" + courseDto.getUpdateDate() + "\",\"createDate\":\"" + courseDto.getCreateDate() + "\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.courseName").value("first"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("TEMPLATE"));
    }

    @Test
    public void testGetCourse() throws Exception {
        CourseDto courseDto = new CourseDto();
        courseDto.setCourseName("first");
        courseDto.setStatus(CourseStatus.TEMPLATE);
        courseDto.setUpdateDate(OffsetDateTime.now(ZoneOffset.UTC));
        courseDto.setCreateDate(OffsetDateTime.now(ZoneOffset.UTC));

        when(authorCourseService.getCourse(1L)).thenReturn(courseDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/author/courses/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.courseName").value("first"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("TEMPLATE"));
    }

    @Test
    public void testUpdateCourse() throws Exception {
        CourseDto courseDto = new CourseDto();
        courseDto.setCourseName("first");
        courseDto.setStatus(CourseStatus.TEMPLATE);
        courseDto.setUpdateDate(OffsetDateTime.now(ZoneOffset.UTC));
        courseDto.setCreateDate(OffsetDateTime.now(ZoneOffset.UTC));

        when(authorCourseService.updateCourse(1L, courseDto)).thenReturn(courseDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/author/courses/1")
                        .content("{\"courseName\":\"first\",\"status\":\"TEMPLATE\"," +
                                "\"updateDate\":\"" + courseDto.getUpdateDate() + "\",\"createDate\":\"" + courseDto.getCreateDate() + "\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.courseName").value("first"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("TEMPLATE"));
    }

    @Test
    public void testDeleteCourse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/author/courses/1"))
                .andExpect(status().isOk());
    }
}