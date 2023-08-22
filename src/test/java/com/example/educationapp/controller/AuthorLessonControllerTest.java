package com.example.educationapp.controller;

import com.example.educationapp.dto.request.RequestLessonDto;
import com.example.educationapp.dto.response.ResponseLessonDto;
import com.example.educationapp.entity.LessonStatus;
import com.example.educationapp.service.AuthorLessonService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthorLessonController.class)
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
        ResponseLessonDto responseLessonDto1 = new ResponseLessonDto(1L, "Lesson 1", "Content 1", LessonStatus.ACTIVE, OffsetDateTime.now(ZoneOffset.UTC), OffsetDateTime.now(ZoneOffset.UTC));
        ResponseLessonDto responseLessonDto2 = new ResponseLessonDto(2L, "Lesson 2", "Content 2", LessonStatus.ACTIVE, OffsetDateTime.now(ZoneOffset.UTC), OffsetDateTime.now(ZoneOffset.UTC));

        lessons.add(responseLessonDto1);
        lessons.add(responseLessonDto2);

        when(authorLessonService.getAllLessons(anyLong())).thenReturn(lessons);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/author/lessons/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(lessons.size()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].lessonName").value(responseLessonDto1.getLessonName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].content").value(responseLessonDto1.getContent()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].lessonStatus").value(responseLessonDto1.getLessonStatus().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].createDate").value(objectMapper.writeValueAsString(responseLessonDto1.getCreateDate())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].updateDate").value(objectMapper.writeValueAsString(responseLessonDto1.getUpdateDate())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].lessonName").value(responseLessonDto2.getLessonName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].content").value(responseLessonDto2.getContent()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].lessonStatus").value(responseLessonDto2.getLessonStatus().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].createDate").value(objectMapper.writeValueAsString(responseLessonDto2.getCreateDate())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].updateDate").value(objectMapper.writeValueAsString(responseLessonDto2.getUpdateDate())));
    }

    @Test
    public void testCreateLesson() throws Exception {
        ResponseLessonDto responseLessonDto = new ResponseLessonDto(1L,"New Lesson", "New Content", LessonStatus.ACTIVE, OffsetDateTime.now(ZoneOffset.UTC), OffsetDateTime.now(ZoneOffset.UTC));

        when(authorLessonService.createLesson(anyLong(), any(RequestLessonDto.class))).thenReturn(responseLessonDto);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/author/lessons/1")
                        .content("{\"lessonName\":\"New Lesson\",\"content\":\"New Content\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.lessonName").value(responseLessonDto.getLessonName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value(responseLessonDto.getContent()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lessonStatus").value(responseLessonDto.getLessonStatus().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.createDate").value(objectMapper.writeValueAsString(responseLessonDto.getCreateDate())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.updateDate").value(objectMapper.writeValueAsString(responseLessonDto.getUpdateDate())));
    }

    @Test
    public void testGetLesson() throws Exception {
        ResponseLessonDto responseLessonDto = new ResponseLessonDto(1L,"Lesson 1", "Content 1", LessonStatus.ACTIVE, OffsetDateTime.now(ZoneOffset.UTC), OffsetDateTime.now(ZoneOffset.UTC));

        when(authorLessonService.getLesson(anyLong(), anyLong())).thenReturn(responseLessonDto);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/author/lessons/1/2"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.lessonName").value(responseLessonDto.getLessonName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value(responseLessonDto.getContent()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lessonStatus").value(responseLessonDto.getLessonStatus().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.createDate").value(objectMapper.writeValueAsString(responseLessonDto.getCreateDate())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.updateDate").value(objectMapper.writeValueAsString(responseLessonDto.getUpdateDate())));
    }

    @Test
    public void testUpdateLesson() throws Exception {
        ResponseLessonDto responseLessonDto = new ResponseLessonDto(1L, "Updated Lesson", "Updated Content", LessonStatus.ACTIVE, OffsetDateTime.now(ZoneOffset.UTC), OffsetDateTime.now(ZoneOffset.UTC));

        when(authorLessonService.updateLesson(anyLong(), anyLong(), any(RequestLessonDto.class))).thenReturn(responseLessonDto);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/author/lessons/1/2")
                        .content("{\"lessonName\":\"Updated Lesson\",\"content\":\"Updated Content\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.lessonName").value(responseLessonDto.getLessonName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value(responseLessonDto.getContent()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lessonStatus").value(responseLessonDto.getLessonStatus().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.createDate").value(objectMapper.writeValueAsString(responseLessonDto.getCreateDate())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.updateDate").value(objectMapper.writeValueAsString(responseLessonDto.getUpdateDate())));
    }

    @Test
    public void testDeleteLesson() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/author/lessons/1/2"))
                .andExpect(status().isOk());
    }
}
