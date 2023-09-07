package com.example.educationapp.controller.student;

import com.example.educationapp.dto.request.student.RequestHomeworkDoneStudentDto;
import com.example.educationapp.dto.response.student.ResponseCourseStudentDto;
import com.example.educationapp.dto.response.student.ResponseHomeworkDoneStudentDto;
import com.example.educationapp.dto.response.student.ResponseHomeworkTaskStudentDto;
import com.example.educationapp.dto.response.student.ResponseLessonStudentDto;
import com.example.educationapp.service.student.StudentService;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@WithMockUser(username = "Lipsar", authorities = "STUDENT")
@AutoConfigureMockMvc
@ActiveProfiles("test")
class StudentControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    StudentService studentService;

    @Test
    void getAllCoursesForStudent() throws Exception {
        ResponseCourseStudentDto courseDto = new ResponseCourseStudentDto();
        courseDto.setId(1L);
        courseDto.setCourseName("Math");
        List<ResponseCourseStudentDto> courseList = Collections.singletonList(courseDto);

        when(studentService.getAllCoursesForStudent()).thenReturn(courseList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/student/courses")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].courseName").value("Math"));
    }

    @Test
    void getCourseInfoForStudent() throws Exception {
        ResponseCourseStudentDto courseDto = new ResponseCourseStudentDto();
        courseDto.setId(1L);
        courseDto.setCourseName("Math");

        when(studentService.getCourseInfoForStudent(1L)).thenReturn(courseDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/student/courses/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.courseName").value("Math"));
    }

    @Test
    void getLessonInfoForStudent() throws Exception{
        ResponseLessonStudentDto lessonDto = new ResponseLessonStudentDto();
        lessonDto.setId(1L);
        lessonDto.setLessonName("Introduction to Algebra");
        List<ResponseLessonStudentDto> lessonList = Collections.singletonList(lessonDto);

        when(studentService.getLessonInfoForStudent(1L, 1L)).thenReturn(lessonDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/student/courses/1/lessons/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lessonName").value("Introduction to Algebra"));
    }

    @Test
    void getHomeworkTaskInfoForStudent() throws Exception{
        ResponseHomeworkTaskStudentDto homeworkTaskDto = new ResponseHomeworkTaskStudentDto();
        homeworkTaskDto.setId(1L);
        homeworkTaskDto.setTitle("Homework 1");
        List<ResponseHomeworkTaskStudentDto> homeworkTaskList = Collections.singletonList(homeworkTaskDto);

        when(studentService.getHomeworkTaskInfoForStudent(1L, 1L, 1L)).thenReturn(homeworkTaskDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/student/courses/1/lessons/1/homeworks/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Homework 1"));
    }

    @Test
    void getStudentHomeworkDone() throws Exception{
        ResponseHomeworkDoneStudentDto homeworkDoneDto = new ResponseHomeworkDoneStudentDto();
        homeworkDoneDto.setId(1L);
        homeworkDoneDto.setSubmissionDate(LocalDateTime.now());
        List<ResponseHomeworkDoneStudentDto> homeworkDoneList = Collections.singletonList(homeworkDoneDto);

        when(studentService.getStudentHomeworkDone(1L, 1L, 1L)).thenReturn(homeworkDoneDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/student/courses/1/lessons/1/homeworks/1/my-homework")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.submissionDate").exists());
    }

    @Test
    void createStudentHomeworkDone() throws Exception{
        RequestHomeworkDoneStudentDto requestDto = new RequestHomeworkDoneStudentDto();
        requestDto.setStudentDescription("My homework");

        ResponseHomeworkDoneStudentDto responseDto = new ResponseHomeworkDoneStudentDto();
        responseDto.setId(1L);
        responseDto.setSubmissionDate(LocalDateTime.now());
        responseDto.setStudentDescription(requestDto.getStudentDescription());

        when(studentService.createHomeworkDone(1L, 1L, 1L, requestDto)).thenReturn(responseDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/student/courses/1/lessons/1/homeworks/1/my-homework")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"studentDescription\":\"My homework\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void editStudentHomeworkDone() throws Exception{
        RequestHomeworkDoneStudentDto requestDto = new RequestHomeworkDoneStudentDto();
        requestDto.setStudentDescription("Updated homework");

        ResponseHomeworkDoneStudentDto responseDto = new ResponseHomeworkDoneStudentDto();
        responseDto.setId(1L);
        responseDto.setSubmissionDate(LocalDateTime.now());
        responseDto.setStudentDescription(requestDto.getStudentDescription());

        when(studentService.editStudentHomeworkDone(1L, 1L, 1L, requestDto)).thenReturn(responseDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/student/courses/1/lessons/1/homeworks/1/my-homework")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"studentDescription\":\"Updated homework\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}