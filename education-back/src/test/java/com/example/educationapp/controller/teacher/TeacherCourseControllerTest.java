package com.example.educationapp.controller.teacher;

import com.example.educationapp.dto.response.ResponseCourseDto;
import com.example.educationapp.service.teacher.TeacherCourseService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.OK;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@WithMockUser(username = "Lipsar", authorities = "TEACHER")
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class TeacherCourseControllerTest {

    @InjectMocks
    private TeacherCourseController teacherCourseController;

    @Mock
    private TeacherCourseService teacherCourseService;

    @Test
    public void testGetAllCoursesForAuthor() {
        // Arrange
        List<ResponseCourseDto> courses = new ArrayList<>();
        when(teacherCourseService.getAllCoursesForTeacher()).thenReturn(courses);

        // Act
        ResponseEntity<List<ResponseCourseDto>> response = teacherCourseController.getAllCoursesForAuthor();

        // Assert
        assertEquals(OK, response.getStatusCode());
        assertEquals(courses, response.getBody());
    }

    @Test
    public void testGetCourse() {
        // Arrange
        Long courseId = 1L;
        ResponseCourseDto course = new ResponseCourseDto();
        when(teacherCourseService.getCourse(courseId)).thenReturn(course);

        // Act
        ResponseEntity<ResponseCourseDto> response = teacherCourseController.getCourse(courseId);

        // Assert
        assertEquals(OK, response.getStatusCode());
        assertEquals(course, response.getBody());
    }
}
