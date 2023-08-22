package com.example.educationapp.controller;

import com.example.educationapp.dto.request.RequestHomeworkTaskDto;
import com.example.educationapp.dto.response.ResponseHomeworkTaskDto;
import com.example.educationapp.service.AuthorHomeworkTaskService;
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

@WebMvcTest(AuthorHomeworkTaskController.class)
public class AuthorHomeworkTaskControllerTest {

    @MockBean
    private AuthorHomeworkTaskService authorHomeworkTaskService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(new AuthorHomeworkTaskController(authorHomeworkTaskService)).build();
    }

    @Test
    public void testGetAllTasks() throws Exception {
        List<ResponseHomeworkTaskDto> tasks = new ArrayList<>();
        ResponseHomeworkTaskDto responseHomeworkTaskDto1 = new ResponseHomeworkTaskDto(1L, "Task 1", "Description 1", OffsetDateTime.now(), OffsetDateTime.now(ZoneOffset.UTC), OffsetDateTime.now(ZoneOffset.UTC));
        ResponseHomeworkTaskDto responseHomeworkTaskDto2 = new ResponseHomeworkTaskDto(2L, "Task 2", "Description 2", OffsetDateTime.now(), OffsetDateTime.now(ZoneOffset.UTC), OffsetDateTime.now(ZoneOffset.UTC));

        tasks.add(responseHomeworkTaskDto1);
        tasks.add(responseHomeworkTaskDto2);

        when(authorHomeworkTaskService.getAllTasks(anyLong(), anyLong())).thenReturn(tasks);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/author/homework-tasks/1/2"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(tasks.size()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value(responseHomeworkTaskDto1.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value(responseHomeworkTaskDto1.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].deadlineDate").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].createDate").value(objectMapper.writeValueAsString(responseHomeworkTaskDto1.getCreateDate())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].updateDate").value(objectMapper.writeValueAsString(responseHomeworkTaskDto1.getUpdateDate())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].title").value(responseHomeworkTaskDto2.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].description").value(responseHomeworkTaskDto2.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].deadlineDate").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].createDate").value(objectMapper.writeValueAsString(responseHomeworkTaskDto2.getCreateDate())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].updateDate").value(objectMapper.writeValueAsString(responseHomeworkTaskDto2.getUpdateDate())));
    }

    @Test
    public void testCreateTask() throws Exception {
        ResponseHomeworkTaskDto responseHomeworkTaskDto = new ResponseHomeworkTaskDto(1L, "New Task", "New Description", OffsetDateTime.now(), OffsetDateTime.now(ZoneOffset.UTC), OffsetDateTime.now(ZoneOffset.UTC));

        when(authorHomeworkTaskService.createTask(anyLong(), anyLong(), any(RequestHomeworkTaskDto.class))).thenReturn(responseHomeworkTaskDto);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/author/homework-tasks/1/2")
                        .content("{\"title\":\"New Task\",\"description\":\"New Description\",\"updateDate\":\"2023-08-16T12:00:00Z\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(responseHomeworkTaskDto.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(responseHomeworkTaskDto.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.deadlineDate").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.createDate").value(objectMapper.writeValueAsString(responseHomeworkTaskDto.getCreateDate())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.updateDate").value(objectMapper.writeValueAsString(responseHomeworkTaskDto.getUpdateDate())));
    }

    @Test
    public void testGetTask() throws Exception {
        ResponseHomeworkTaskDto responseHomeworkTaskDto = new ResponseHomeworkTaskDto(1L, "Task 1", "Description 1", OffsetDateTime.now(), OffsetDateTime.now(ZoneOffset.UTC), OffsetDateTime.now(ZoneOffset.UTC));

        when(authorHomeworkTaskService.getTask(anyLong(), anyLong(), anyLong())).thenReturn(responseHomeworkTaskDto);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/author/homework-tasks/1/2/3"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(responseHomeworkTaskDto.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(responseHomeworkTaskDto.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.deadlineDate").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.createDate").value(objectMapper.writeValueAsString(responseHomeworkTaskDto.getCreateDate())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.updateDate").value(objectMapper.writeValueAsString(responseHomeworkTaskDto.getUpdateDate())));
    }

    @Test
    public void testUpdateTask() throws Exception {
        ResponseHomeworkTaskDto responseHomeworkTaskDto = new ResponseHomeworkTaskDto(1L, "Updated Task", "Updated Description", OffsetDateTime.now(), OffsetDateTime.now(ZoneOffset.UTC), OffsetDateTime.now(ZoneOffset.UTC));

        when(authorHomeworkTaskService.updateTask(anyLong(), anyLong(), anyLong(), any(RequestHomeworkTaskDto.class))).thenReturn(responseHomeworkTaskDto);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/author/homework-tasks/1/2/3")
                        .content("{\"title\":\"Updated Task\",\"description\":\"Updated Description\",\"updateDate\":\"2023-08-16T12:00:00Z\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(responseHomeworkTaskDto.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(responseHomeworkTaskDto.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.deadlineDate").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.createDate").value(objectMapper.writeValueAsString(responseHomeworkTaskDto.getCreateDate())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.updateDate").value(objectMapper.writeValueAsString(responseHomeworkTaskDto.getUpdateDate())));
    }

    @Test
    public void testDeleteTask() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/author/homework-tasks/1/2/3"))
                .andExpect(status().isOk());
    }
}
