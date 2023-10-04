package com.example.educationapp.controller.teacher;

import com.example.educationapp.dto.response.ResponseLessonDto;
import com.example.educationapp.entity.LessonStatus;
import com.example.educationapp.service.teacher.TeacherLessonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@WithMockUser(username = "Lipsar", authorities = "TEACHER")
@AutoConfigureMockMvc
public class TeacherLessonControllerTest {

    @MockBean
    private TeacherLessonService teacherLessonService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetAllLessons() throws Exception {
        List<ResponseLessonDto> lessons = new ArrayList<>();
        ResponseLessonDto responseLessonDto1 = new ResponseLessonDto(1L, "Lesson 1", "Content 1", LessonStatus.ACTIVE, OffsetDateTime.now(ZoneOffset.UTC), OffsetDateTime.now(ZoneOffset.UTC));
        ResponseLessonDto responseLessonDto2 = new ResponseLessonDto(2L, "Lesson 2", "Content 2", LessonStatus.ACTIVE, OffsetDateTime.now(ZoneOffset.UTC), OffsetDateTime.now(ZoneOffset.UTC));

        lessons.add(responseLessonDto1);
        lessons.add(responseLessonDto2);

        when(teacherLessonService.getAllLessons(anyLong())).thenReturn(lessons);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/teacher/lessons/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(lessons.size()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].lessonName").value(responseLessonDto1.getLessonName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].content").value(responseLessonDto1.getContent()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].lessonStatus").value(responseLessonDto1.getLessonStatus().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].createDate").value(responseLessonDto1.getCreateDate().format(DateTimeFormatter.ISO_DATE_TIME)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].updateDate").value(responseLessonDto1.getUpdateDate().format(DateTimeFormatter.ISO_DATE_TIME)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].lessonName").value(responseLessonDto2.getLessonName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].content").value(responseLessonDto2.getContent()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].lessonStatus").value(responseLessonDto2.getLessonStatus().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].createDate").value(responseLessonDto2.getCreateDate().format(DateTimeFormatter.ISO_DATE_TIME)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].updateDate").value(responseLessonDto2.getUpdateDate().format(DateTimeFormatter.ISO_DATE_TIME)));
    }

    @Test
    public void testGetLesson() throws Exception {
        ResponseLessonDto responseLessonDto = new ResponseLessonDto(1L, "Lesson 1", "Content 1", LessonStatus.ACTIVE, OffsetDateTime.now(ZoneOffset.UTC), OffsetDateTime.now(ZoneOffset.UTC));

        when(teacherLessonService.getLesson(anyLong(), anyLong())).thenReturn(responseLessonDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/teacher/lessons/1/2"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.lessonName").value(responseLessonDto.getLessonName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value(responseLessonDto.getContent()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lessonStatus").value(responseLessonDto.getLessonStatus().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.createDate").value(responseLessonDto.getCreateDate().format(DateTimeFormatter.ISO_DATE_TIME)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.updateDate").value(responseLessonDto.getUpdateDate().format(DateTimeFormatter.ISO_DATE_TIME)));
    }
}
