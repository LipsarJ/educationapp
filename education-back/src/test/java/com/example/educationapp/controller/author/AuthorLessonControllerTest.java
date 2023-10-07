package com.example.educationapp.controller.author;

import com.example.educationapp.controlleradvice.Errors;
import com.example.educationapp.dto.request.RequestLessonDto;
import com.example.educationapp.dto.response.ResponseLessonDto;
import com.example.educationapp.entity.LessonStatus;
import com.example.educationapp.exception.extend.InvalidStatusException;
import com.example.educationapp.exception.extend.LessonNotFoundException;
import com.example.educationapp.service.author.AuthorLessonService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
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

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@WithMockUser(username = "Lipsar", authorities = "AUTHOR")
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthorLessonControllerTest {

    @MockBean
    private AuthorLessonService authorLessonService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAllLessons() throws Exception {
        List<ResponseLessonDto> lessons = new ArrayList<>();
        ResponseLessonDto responseLessonDto1 = new ResponseLessonDto(1L, "Lesson 1", "Content 1", 1, LessonStatus.ACTIVE, OffsetDateTime.now(ZoneOffset.UTC), OffsetDateTime.now(ZoneOffset.UTC));
        ResponseLessonDto responseLessonDto2 = new ResponseLessonDto(2L, "Lesson 2", "Content 2", 1, LessonStatus.ACTIVE, OffsetDateTime.now(ZoneOffset.UTC), OffsetDateTime.now(ZoneOffset.UTC));

        lessons.add(responseLessonDto1);
        lessons.add(responseLessonDto2);

        when(authorLessonService.getAllLessons(anyLong())).thenReturn(lessons);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/author/lessons/1"))
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
    public void testCreateLesson() throws Exception {
        ResponseLessonDto responseLessonDto = new ResponseLessonDto(1L, "New Lesson", "New Content", 1, LessonStatus.ACTIVE, OffsetDateTime.now(ZoneOffset.UTC), OffsetDateTime.now(ZoneOffset.UTC));

        when(authorLessonService.createLesson(anyLong(), any(RequestLessonDto.class))).thenReturn(responseLessonDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/author/lessons/1").with(csrf())
                        .content("{\"lessonName\":\"New Lesson\",\"content\":\"New Content\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.lessonName").value(responseLessonDto.getLessonName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value(responseLessonDto.getContent()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lessonStatus").value(responseLessonDto.getLessonStatus().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.createDate").value(responseLessonDto.getCreateDate().format(DateTimeFormatter.ISO_DATE_TIME)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.updateDate").value(responseLessonDto.getUpdateDate().format(DateTimeFormatter.ISO_DATE_TIME)));
    }

    @Test
    public void testGetLesson() throws Exception {
        ResponseLessonDto responseLessonDto = new ResponseLessonDto(1L, "Lesson 1", "Content 1", 1, LessonStatus.ACTIVE, OffsetDateTime.now(ZoneOffset.UTC), OffsetDateTime.now(ZoneOffset.UTC));

        when(authorLessonService.getLesson(anyLong(), anyLong())).thenReturn(responseLessonDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/author/lessons/1/2"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.lessonName").value(responseLessonDto.getLessonName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value(responseLessonDto.getContent()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lessonStatus").value(responseLessonDto.getLessonStatus().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.createDate").value(responseLessonDto.getCreateDate().format(DateTimeFormatter.ISO_DATE_TIME)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.updateDate").value(responseLessonDto.getUpdateDate().format(DateTimeFormatter.ISO_DATE_TIME)));
    }

    @Test
    public void testUpdateLesson() throws Exception {
        ResponseLessonDto responseLessonDto = new ResponseLessonDto(1L, "Updated Lesson", "Updated Content", 1, LessonStatus.ACTIVE, OffsetDateTime.now(ZoneOffset.UTC), OffsetDateTime.now(ZoneOffset.UTC));

        when(authorLessonService.updateLesson(anyLong(), anyLong(), any(RequestLessonDto.class))).thenReturn(responseLessonDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/author/lessons/1/2").with(csrf())
                        .content("{\"lessonName\":\"Updated Lesson\",\"content\":\"Updated Content\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.lessonName").value(responseLessonDto.getLessonName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value(responseLessonDto.getContent()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lessonStatus").value(responseLessonDto.getLessonStatus().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.createDate").value(responseLessonDto.getCreateDate().format(DateTimeFormatter.ISO_DATE_TIME)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.updateDate").value(responseLessonDto.getUpdateDate().format(DateTimeFormatter.ISO_DATE_TIME)));
    }

    @Test
    public void testDeleteLesson() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/author/lessons/1/2").with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    public void testCreateLessonInvalidStatusException() throws Exception {
        RequestLessonDto requestDto = new RequestLessonDto();
        requestDto.setLessonName("Test Lesson");
        requestDto.setLessonStatus(LessonStatus.NOT_ACTIVE);

        ObjectMapper objectMapper = new ObjectMapper();

        when(authorLessonService.createLesson(anyLong(), eq(requestDto)))
                .thenThrow(new InvalidStatusException("Lesson can be only created with Active status.", Errors.STATUS_IS_INVALID));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/author/lessons/{courseId}", 1L).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Lesson can be only created with Active status."));
    }

    @Test
    public void testDeleteLessonLessonNotFoundException() throws Exception {
        Long courseId = 1L;
        Long id = 2L;

        doThrow(new LessonNotFoundException("Lesson is not found."))
                .when(authorLessonService).deleteLesson(courseId, id);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/author/lessons/{courseId}/{id}", courseId, id).with(csrf()))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Lesson is not found."));
    }

    @Test
    @WithMockUser(username = "studentTest", authorities = "STUDENT")
    public void testGetLessonAuthoritiesCheckStudent() throws Exception {
        ResponseLessonDto responseLessonDto = new ResponseLessonDto(1L, "Lesson 1", "Content 1", 1, LessonStatus.ACTIVE, OffsetDateTime.now(ZoneOffset.UTC), OffsetDateTime.now(ZoneOffset.UTC));

        when(authorLessonService.getLesson(anyLong(), anyLong())).thenReturn(responseLessonDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/author/lessons/1/2"))
                .andExpect(status().isForbidden());
        ;
    }

    @Test
    @WithMockUser(username = "teacherTest", authorities = "TEACHER")
    public void testGetLessonAuthoritiesCheckTeacher() throws Exception {
        ResponseLessonDto responseLessonDto = new ResponseLessonDto(1L, "Lesson 1", "Content 1", 1, LessonStatus.ACTIVE, OffsetDateTime.now(ZoneOffset.UTC), OffsetDateTime.now(ZoneOffset.UTC));

        when(authorLessonService.getLesson(anyLong(), anyLong())).thenReturn(responseLessonDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/author/lessons/1/2"))
                .andExpect(status().isForbidden());
    }
}
