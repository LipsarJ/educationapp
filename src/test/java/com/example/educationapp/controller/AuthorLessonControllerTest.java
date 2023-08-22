package com.example.educationapp.controller;

import com.example.educationapp.dto.response.ResponseLessonDto;
import com.example.educationapp.entity.LessonStatus;
import com.example.educationapp.service.AuthorLessonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthorLessonControllerTest {
    @MockBean
    private AuthorLessonService authorLessonService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(new AuthorLessonController(authorLessonService)).build();
    }

    @Test
    public void testGetAllLessons() throws Exception {
        List<ResponseLessonDto> lessons = new ArrayList<>();
        ResponseLessonDto lesson1 = new ResponseLessonDto("Lesson 1", "Content 1", LessonStatus.ACTIVE);
        ResponseLessonDto lesson2 = new ResponseLessonDto("Lesson 2", "Content 2", LessonStatus.ACTIVE);

        lessons.add(lesson1);
        lessons.add(lesson2);

        when(authorLessonService.getAllLessons(anyLong())).thenReturn(lessons);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/author/homework-tasks/1/2"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(lessons.size()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].lessonName").value(lesson1.getLessonName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value(lesson1.getContent()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].lessonStatus").value(lesson1.getLessonStatus().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].deadlineDate").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].lessonName").value(lesson2.getLessonName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].description").value(lesson2.getContent()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].lessonStatus").value(lesson2.getLessonStatus().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].deadlineDate").exists());
    }
}
