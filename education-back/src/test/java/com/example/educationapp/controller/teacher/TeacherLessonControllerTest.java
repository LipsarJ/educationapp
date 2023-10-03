package com.example.educationapp.controller.teacher;

import com.example.educationapp.dto.response.ResponseLessonDto;
import com.example.educationapp.service.teacher.TeacherLessonService;
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
public class TeacherLessonControllerTest {

    @InjectMocks
    private TeacherLessonController teacherLessonController;

    @Mock
    private TeacherLessonService teacherLessonService;

    @Test
    public void testGetAllLessons() {
        // Arrange
        Long courseId = 1L;
        List<ResponseLessonDto> lessons = new ArrayList<>();
        when(teacherLessonService.getAllLessons(courseId)).thenReturn(lessons);

        // Act
        ResponseEntity<List<ResponseLessonDto>> response = teacherLessonController.getAllLessons(courseId);

        // Assert
        assertEquals(OK, response.getStatusCode());
        assertEquals(lessons, response.getBody());
    }

    @Test
    public void testGetLesson() {
        // Arrange
        Long courseId = 1L;
        Long lessonId = 2L;
        ResponseLessonDto lesson = new ResponseLessonDto();
        when(teacherLessonService.getLesson(courseId, lessonId)).thenReturn(lesson);

        // Act
        ResponseEntity<ResponseLessonDto> response = teacherLessonController.getLesson(courseId, lessonId);

        // Assert
        assertEquals(OK, response.getStatusCode());
        assertEquals(lesson, response.getBody());
    }
}
