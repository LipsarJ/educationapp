package com.example.educationapp.controller.teacher;

import com.example.educationapp.dto.response.ResponseCourseDto;
import com.example.educationapp.service.teacher.TeacherCourseService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@WithMockUser(username = "Lipsar", authorities = "TEACHER")
@AutoConfigureMockMvc
public class TeacherCourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TeacherCourseService teacherCourseService;

    @Test
    public void testGetAllCoursesForTeacher() throws Exception {
        List<ResponseCourseDto> courses = new ArrayList<>();
        ResponseCourseDto responseCourseDto = new ResponseCourseDto();
        courses.add(responseCourseDto);
        when(teacherCourseService.getAllCoursesForTeacher()).thenReturn(courses);

        mockMvc.perform(get("/api/v1/teacher/courses")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(courses.size())));
    }

    @Test
    public void testGetCourse() throws Exception {
        Long courseId = 1L; // replace with actual ID
        ResponseCourseDto responseCourseDto = new ResponseCourseDto(); // populate with test data
        when(teacherCourseService.getCourse(courseId)).thenReturn(responseCourseDto);

        mockMvc.perform(get("/api/v1/teacher/courses/{id}", courseId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(responseCourseDto.getId()))
                .andExpect(jsonPath("$.courseName").value(responseCourseDto.getCourseName()))
                .andExpect(jsonPath("$.courseStatus").value(responseCourseDto.getCourseStatus()));
    }

}