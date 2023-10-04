package com.example.educationapp.controller.teacher;

import com.example.educationapp.dto.response.ResponseHomeworkTaskDto;
import com.example.educationapp.service.teacher.TeacherHomeworkTaskService;
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
public class TeacherHomeworkTaskControllerTest {

    @MockBean
    private TeacherHomeworkTaskService teacherHomeworkTaskService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetAllTasks() throws Exception {
        List<ResponseHomeworkTaskDto> tasks = new ArrayList<>();
        ResponseHomeworkTaskDto responseHomeworkTaskDto1 = new ResponseHomeworkTaskDto(1L, "Task 1", "Description 1", OffsetDateTime.now(), OffsetDateTime.now(ZoneOffset.UTC), OffsetDateTime.now(ZoneOffset.UTC));
        ResponseHomeworkTaskDto responseHomeworkTaskDto2 = new ResponseHomeworkTaskDto(2L, "Task 2", "Description 2", OffsetDateTime.now(), OffsetDateTime.now(ZoneOffset.UTC), OffsetDateTime.now(ZoneOffset.UTC));

        tasks.add(responseHomeworkTaskDto1);
        tasks.add(responseHomeworkTaskDto2);

        when(teacherHomeworkTaskService.getAllTasks(anyLong(), anyLong())).thenReturn(tasks);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/teacher/homework-tasks/1/2"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(tasks.size()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value(responseHomeworkTaskDto1.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value(responseHomeworkTaskDto1.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].deadlineDate").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].createDate").value(responseHomeworkTaskDto1.getCreateDate().format(DateTimeFormatter.ISO_DATE_TIME)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].updateDate").value(responseHomeworkTaskDto1.getUpdateDate().format(DateTimeFormatter.ISO_DATE_TIME)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].title").value(responseHomeworkTaskDto2.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].description").value(responseHomeworkTaskDto2.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].deadlineDate").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].createDate").value(responseHomeworkTaskDto2.getCreateDate().format(DateTimeFormatter.ISO_DATE_TIME)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].updateDate").value(responseHomeworkTaskDto2.getUpdateDate().format(DateTimeFormatter.ISO_DATE_TIME)));
    }

    @Test
    public void testGetTask() throws Exception {
        ResponseHomeworkTaskDto responseHomeworkTaskDto = new ResponseHomeworkTaskDto(1L, "Task 1", "Description 1", OffsetDateTime.now(), OffsetDateTime.now(ZoneOffset.UTC), OffsetDateTime.now(ZoneOffset.UTC));

        when(teacherHomeworkTaskService.getTask(anyLong(), anyLong(), anyLong())).thenReturn(responseHomeworkTaskDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/teacher/homework-tasks/1/2/3"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(responseHomeworkTaskDto.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(responseHomeworkTaskDto.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.deadlineDate").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.createDate").value(responseHomeworkTaskDto.getCreateDate().format(DateTimeFormatter.ISO_DATE_TIME)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.updateDate").value(responseHomeworkTaskDto.getUpdateDate().format(DateTimeFormatter.ISO_DATE_TIME)));
    }
}
