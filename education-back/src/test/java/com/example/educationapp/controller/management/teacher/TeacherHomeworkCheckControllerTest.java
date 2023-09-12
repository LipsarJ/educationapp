package com.example.educationapp.controller.management.teacher;

import com.example.educationapp.dto.request.teacher.RequestTeacherCheckHomeworkDto;
import com.example.educationapp.dto.response.student.ResponseHomeworkDoneStudentDto;
import com.example.educationapp.service.management.teacher.TeacherHomeworkCheckService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@WithMockUser(username = "Lipsar", authorities = "TEACHER")
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TeacherHomeworkCheckControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    TeacherHomeworkCheckService teacherHomeworkCheckService;

    @Test
    void getAllHomeworksDoneForTask() throws Exception {
        boolean checked = false;
        List<ResponseHomeworkDoneStudentDto> mockDtoList = new ArrayList<>();
        ResponseHomeworkDoneStudentDto responseHomeworkDoneStudentDto1 = new ResponseHomeworkDoneStudentDto();
        responseHomeworkDoneStudentDto1.setId(1L);
        responseHomeworkDoneStudentDto1.setStudentDescription("Desc");
        ResponseHomeworkDoneStudentDto responseHomeworkDoneStudentDto2 = new ResponseHomeworkDoneStudentDto();
        responseHomeworkDoneStudentDto2.setId(2L);
        responseHomeworkDoneStudentDto2.setStudentDescription("Grade");
        responseHomeworkDoneStudentDto2.setGrade(1);
        mockDtoList.add(responseHomeworkDoneStudentDto1);
        mockDtoList.add(responseHomeworkDoneStudentDto2);
        PageImpl<ResponseHomeworkDoneStudentDto> mockPage = new PageImpl<>(mockDtoList);
        when(teacherHomeworkCheckService.getAllHomeworksDoneForTask(anyLong(), anyLong(), anyLong(), any(Pageable.class), eq(checked))).thenReturn(mockPage);

        mockMvc.perform(get("/api/v1/teacher/course/1/lessons/1/homeworks/1?checked=false")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.homeworkDoneInfo").isArray());
    }

    @Test
    void setGradeToHomework() throws Exception {
        RequestTeacherCheckHomeworkDto requestDto = new RequestTeacherCheckHomeworkDto();
        requestDto.setGrade(5);

        ResponseHomeworkDoneStudentDto responseDto = new ResponseHomeworkDoneStudentDto();
        responseDto.setId(1L);
        responseDto.setGrade(5);

        when(teacherHomeworkCheckService.setGradeToHomeworkDone(anyLong(), anyLong(), anyLong(), anyLong(), any()))
                .thenReturn(responseDto);

        mockMvc.perform(put("/api/v1/teacher/course/1/lessons/1/homeworks/1/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"grade\": 5 }")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.grade").value(5));
    }
}