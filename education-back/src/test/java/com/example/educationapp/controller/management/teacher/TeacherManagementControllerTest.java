package com.example.educationapp.controller.management.teacher;

import com.example.educationapp.dto.request.management.teacher.AddOrRemoveStudentsDto;
import com.example.educationapp.dto.response.ResponseUserDto;
import com.example.educationapp.dto.response.UserInfoDto;
import com.example.educationapp.entity.Course;
import com.example.educationapp.entity.User;
import com.example.educationapp.mapper.UserMapper;
import com.example.educationapp.repo.UserRepo;
import com.example.educationapp.security.service.UserContext;
import com.example.educationapp.service.management.teacher.TeacherManagementService;
import com.example.educationapp.utils.CourseUtils;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@WithMockUser(username = "Lipsar", authorities = "TEACHER")
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class TeacherManagementControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    TeacherManagementService teacherManagementService;
    @MockBean
    UserMapper userMapper;
    @MockBean
    ResponseUserDto responseUserDto;
    @MockBean
    CourseUtils courseUtils;
    @MockBean
    UserContext userContext;
    @MockBean
    UserRepo userRepo;

    @Test
    public void testGetAllStudentsForCourse() throws Exception {
        List<UserInfoDto> userInfos = new ArrayList<>();
        userInfos.add(new UserInfoDto(1L, "user1", "John", "Doe", "Smith"));
        Page<UserInfoDto> userPage = new PageImpl<>(userInfos, PageRequest.of(0, 20), userInfos.size());

        Course course = new Course();
        course.setId(1L);
        when(courseUtils.validateAndGetCourseForAuthor(1L)).thenReturn(course);
        when(teacherManagementService.getAllStudentsForCourse(any(), any(Pageable.class))).thenReturn(userPage);
        mockMvc.perform(get("/api/v1/teacher/course/1/students")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userInfo").isArray())
                .andExpect(jsonPath("$.userInfo", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.userInfo[0].id").value(1))
                .andExpect(jsonPath("$.userInfo[0].username").value("user1"))
                .andExpect(jsonPath("$.totalCount").value(userInfos.size()))
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.countValuesPerPage").value(20));
    }

    @Test
    public void testAddStudentsForCourse() throws Exception {
        AddOrRemoveStudentsDto addOrRemoveStudentsDto = new AddOrRemoveStudentsDto();
        addOrRemoveStudentsDto.setIds(Collections.singletonList(1L));

        Course course = new Course();
        course.setId(1L);
        when(courseUtils.validateAndGetCourseForTeacher(eq(1L))).thenReturn(course);

        User user = new User();
        user.setUsername("std1");
        user.setId(1L);
        List<ResponseUserDto> userDtos = new ArrayList<>();
        ResponseUserDto responseUserDto1 = new ResponseUserDto();
        responseUserDto1.setId(1L);
        userDtos.add(responseUserDto1);

        when(userRepo.findByIdIn(Collections.singletonList(1L))).thenReturn(Collections.singleton(user));
        when(teacherManagementService.addStudentsForCourse(eq(1L), eq(addOrRemoveStudentsDto))).thenReturn(userDtos);
        mockMvc.perform(put("/api/v1/teacher/course/1/add-students", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(addOrRemoveStudentsDto)))
                .andExpect(status().isOk());
    }

    @Test
    public void testRemoveStudentsForCourse() throws Exception {
        AddOrRemoveStudentsDto addOrRemoveStudentsDto = new AddOrRemoveStudentsDto();
        addOrRemoveStudentsDto.setIds(Collections.singletonList(1L));

        Course course = new Course();
        course.setId(1L);
        when(courseUtils.validateAndGetCourseForTeacher(eq(1L))).thenReturn(course);

        User user = new User();
        user.setUsername("std1");
        user.setId(1L);
        List<ResponseUserDto> userDtos = new ArrayList<>();
        ResponseUserDto responseUserDto1 = new ResponseUserDto();
        responseUserDto1.setId(1L);
        userDtos.add(responseUserDto1);

        when(userRepo.findByIdIn(Collections.singletonList(1L))).thenReturn(Collections.singleton(user));
        when(teacherManagementService.removeStudentsForCourse(eq(1L), eq(addOrRemoveStudentsDto))).thenReturn(userDtos);
        mockMvc.perform(put("/api/v1/teacher/course/1/remove-students", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(addOrRemoveStudentsDto)))
                .andExpect(status().isOk()); // Проверьте ожидаемый ответ
    }
}
