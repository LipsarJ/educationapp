package com.example.educationapp.controller;

import com.example.educationapp.dto.request.RequestHomeworkTaskDto;
import com.example.educationapp.dto.response.ResponseHomeworkTaskDto;
import com.example.educationapp.service.AuthorHomeworkTaskService;
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
        ResponseHomeworkTaskDto task1 = new ResponseHomeworkTaskDto("Task 1", "Description 1", OffsetDateTime.now());
        ResponseHomeworkTaskDto task2 = new ResponseHomeworkTaskDto("Task 2", "Description 2", OffsetDateTime.now());

        tasks.add(task1);
        tasks.add(task2);

        when(authorHomeworkTaskService.getAllTasks(anyLong(), anyLong())).thenReturn(tasks);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/author/homework-tasks/1/2"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(tasks.size()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value(task1.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value(task1.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].deadlineDate").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].title").value(task2.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].description").value(task2.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].deadlineDate").exists());
    }

    @Test
    public void testCreateTask() throws Exception {
        ResponseHomeworkTaskDto taskDto = new ResponseHomeworkTaskDto("New Task", "New Description", OffsetDateTime.now());

        when(authorHomeworkTaskService.createTask(anyLong(), anyLong(), any(RequestHomeworkTaskDto.class))).thenReturn(taskDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/author/homework-tasks/1/2")
                        .content("{\"title\":\"New Task\",\"description\":\"New Description\",\"updateDate\":\"2023-08-16T12:00:00Z\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(taskDto.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(taskDto.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.deadlineDate").exists());
    }

    @Test
    public void testGetTask() throws Exception {
        ResponseHomeworkTaskDto taskDto = new ResponseHomeworkTaskDto("Task 1", "Description 1", OffsetDateTime.now());

        when(authorHomeworkTaskService.getTask(anyLong(), anyLong(), anyLong())).thenReturn(taskDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/author/homework-tasks/1/2/3"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(taskDto.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(taskDto.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.deadlineDate").exists());
    }

    @Test
    public void testUpdateTask() throws Exception {
        ResponseHomeworkTaskDto taskDto = new ResponseHomeworkTaskDto("Updated Task", "Updated Description", OffsetDateTime.now());

        when(authorHomeworkTaskService.updateTask(anyLong(), anyLong(), anyLong(), any(RequestHomeworkTaskDto.class))).thenReturn(taskDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/author/homework-tasks/1/2/3")
                        .content("{\"title\":\"Updated Task\",\"description\":\"Updated Description\",\"updateDate\":\"2023-08-16T12:00:00Z\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(taskDto.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(taskDto.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.deadlineDate").exists());
    }

    @Test
    public void testDeleteTask() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/author/homework-tasks/1/2/3"))
                .andExpect(status().isOk());
    }
}
