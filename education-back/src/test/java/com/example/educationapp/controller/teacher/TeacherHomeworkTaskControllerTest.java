package com.example.educationapp.controller.teacher;

import com.example.educationapp.dto.response.ResponseHomeworkTaskDto;
import com.example.educationapp.service.teacher.TeacherHomeworkTaskService;
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
public class TeacherHomeworkTaskControllerTest {

    @InjectMocks
    private TeacherHomeworkTaskController teacherHomeworkTaskController;

    @Mock
    private TeacherHomeworkTaskService teacherHomeworkTaskService;

    @Test
    public void testGetAllTasks() {
        // Arrange
        Long courseId = 1L;
        Long lessonId = 2L;
        List<ResponseHomeworkTaskDto> tasks = new ArrayList<>();
        when(teacherHomeworkTaskService.getAllTasks(courseId, lessonId)).thenReturn(tasks);

        // Act
        ResponseEntity<List<ResponseHomeworkTaskDto>> response = teacherHomeworkTaskController.getAllTasks(courseId, lessonId);

        // Assert
        assertEquals(OK, response.getStatusCode());
        assertEquals(tasks, response.getBody());
    }

    @Test
    public void testGetTask() {
        // Arrange
        Long courseId = 1L;
        Long lessonId = 2L;
        Long taskId = 3L;
        ResponseHomeworkTaskDto task = new ResponseHomeworkTaskDto();
        when(teacherHomeworkTaskService.getTask(courseId, lessonId, taskId)).thenReturn(task);

        // Act
        ResponseEntity<ResponseHomeworkTaskDto> response = teacherHomeworkTaskController.getTask(courseId, lessonId, taskId);

        // Assert
        assertEquals(OK, response.getStatusCode());
        assertEquals(task, response.getBody());
    }
}
