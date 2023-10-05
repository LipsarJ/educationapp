package com.example.educationapp.controller.management.author;

import com.example.educationapp.dto.request.management.author.AddOrRemoveAuthorsDto;
import com.example.educationapp.dto.request.management.author.AddOrRemoveTeachersDto;
import com.example.educationapp.dto.response.UserInfoDto; // Заменен импорт
import com.example.educationapp.entity.Course;
import com.example.educationapp.entity.User;
import com.example.educationapp.mapper.UserMapper;
import com.example.educationapp.repo.UserRepo;
import com.example.educationapp.security.service.UserContext;
import com.example.educationapp.service.management.author.AuthorManagementService;
import com.example.educationapp.utils.CourseUtils;
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
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@WithMockUser(username = "Lipsar", authorities = "AUTHOR")
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthorManagementControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    AuthorManagementService authorManagementService;
    @MockBean
    UserMapper userMapper;
    @MockBean
    UserInfoDto userInfoDto; // Заменен ResponseUserDto
    @MockBean
    CourseUtils courseUtils;
    @MockBean
    UserContext userContext;
    @MockBean
    UserRepo userRepo;

    @Test
    public void testGetAllTwoAuthorsForCourse() throws Exception {
        User user1 = new User();
        user1.setUsername("author1");
        User user2 = new User();
        user2.setUsername("author2");

        Course course = new Course();
        course.setId(1L);
        course.getAuthors().add(user1);
        course.getAuthors().add(user2);
        List<UserInfoDto> authors = new ArrayList<>(); // Заменен ResponseUserDto
        authors.add(userMapper.toUserInfoDto(user1)); // Заменен toResponseUserDto
        authors.add(userMapper.toUserInfoDto(user2)); // Заменен toResponseUserDto
        when(courseUtils.validateAndGetCourseForAuthor(1L)).thenReturn(course);
        when(authorManagementService.getAllAuthorsForCourse(1L)).thenReturn(authors);
        mockMvc.perform(get("/api/v1/author/courses/1/authors")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void testGetAllThreeAuthorsForCourse() throws Exception {
        User user1 = new User();
        user1.setUsername("author1");
        User user2 = new User();
        user2.setUsername("author2");
        User user3 = new User();
        user1.setUsername("author3");

        Course course = new Course();
        course.setId(1L);
        course.getAuthors().add(user1);
        course.getAuthors().add(user2);
        course.getAuthors().add(user3);
        List<UserInfoDto> authors = new ArrayList<>(); // Заменен ResponseUserDto
        authors.add(userMapper.toUserInfoDto(user1)); // Заменен toResponseUserDto
        authors.add(userMapper.toUserInfoDto(user2)); // Заменен toResponseUserDto
        authors.add(userMapper.toUserInfoDto(user3)); // Заменен toResponseUserDto
        when(courseUtils.validateAndGetCourseForAuthor(1L)).thenReturn(course);
        when(authorManagementService.getAllAuthorsForCourse(1L)).thenReturn(authors);
        mockMvc.perform(get("/api/v1/author/courses/1/authors")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    public void testAddAuthorsToCourse() throws Exception {
        User user1 = new User();
        user1.setUsername("author1");
        user1.setId(1L);
        User user2 = new User();
        user2.setUsername("author2");
        user2.setId(2L);
        Set<User> authorsSet = new HashSet<>();
        authorsSet.add(user1);
        authorsSet.add(user2);
        AddOrRemoveAuthorsDto addOrRemoveAuthorsDto = new AddOrRemoveAuthorsDto(Arrays.asList(1L, 2L));
        Course course = new Course();
        course.setId(1L);
        List<UserInfoDto> authors = new ArrayList<>(); // Заменен ResponseUserDto
        authors.add(userMapper.toUserInfoDto(user1)); // Заменен toResponseUserDto
        authors.add(userMapper.toUserInfoDto(user2)); // Заменен toResponseUserDto
        when(userRepo.findByIdIn(addOrRemoveAuthorsDto.getIds())).thenReturn(authorsSet);
        when(courseUtils.validateAndGetCourseForAuthor(1L)).thenReturn(course);
        when(authorManagementService.addAuthorsForCourse(1L, addOrRemoveAuthorsDto)).thenReturn(authors);

        mockMvc.perform(put("/api/v1/author/courses/1/add-authors")
                        .with(csrf())
                        .content(new ObjectMapper().writeValueAsString(addOrRemoveAuthorsDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testRemoveAuthorsToCourse() throws Exception {
        User user1 = new User();
        user1.setUsername("author1");
        user1.setId(1L);
        User user2 = new User();
        user2.setUsername("author2");
        user2.setId(2L);
        Set<User> authorsSet = new HashSet<>();
        authorsSet.add(user1);
        authorsSet.add(user2);
        AddOrRemoveAuthorsDto addOrRemoveAuthorsDto = new AddOrRemoveAuthorsDto(Arrays.asList(1L, 2L));
        Course course = new Course();
        course.setId(1L);
        List<UserInfoDto> authors = new ArrayList<>(); // Заменен ResponseUserDto
        authors.add(userMapper.toUserInfoDto(user1)); // Заменен toResponseUserDto
        authors.add(userMapper.toUserInfoDto(user2)); // Заменен toResponseUserDto
        when(userRepo.findByIdIn(addOrRemoveAuthorsDto.getIds())).thenReturn(authorsSet);
        when(courseUtils.validateAndGetCourseForAuthor(1L)).thenReturn(course);
        when(authorManagementService.addAuthorsForCourse(1L, addOrRemoveAuthorsDto)).thenReturn(authors);

        mockMvc.perform(put("/api/v1/author/courses/1/remove-authors")
                        .with(csrf())
                        .content(new ObjectMapper().writeValueAsString(addOrRemoveAuthorsDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetAllTwoTeachersForCourse() throws Exception {
        User user1 = new User();
        user1.setUsername("teacher1");
        User user2 = new User();
        user2.setUsername("teacher2");

        Course course = new Course();
        course.setId(1L);
        course.getTeachers().add(user1);
        course.getTeachers().add(user2);
        List<UserInfoDto> teachers = new ArrayList<>(); // Заменен ResponseUserDto
        teachers.add(userMapper.toUserInfoDto(user1)); // Заменен toResponseUserDto
        teachers.add(userMapper.toUserInfoDto(user2)); // Заменен toResponseUserDto
        when(courseUtils.validateAndGetCourseForAuthor(1L)).thenReturn(course);
        when(authorManagementService.getAllTeachersForCourse(1L)).thenReturn(teachers);
        mockMvc.perform(get("/api/v1/author/courses/1/teachers")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void testGetAllThreeTeachersForCourse() throws Exception {
        User user1 = new User();
        user1.setUsername("teacher1");
        User user2 = new User();
        user2.setUsername("teacher2");
        User user3 = new User();
        user3.setUsername("teacher3"); // Исправлена ошибка в имени пользователя

        Course course = new Course();
        course.setId(1L);
        course.getTeachers().add(user1);
        course.getTeachers().add(user2);
        course.getTeachers().add(user3);
        List<UserInfoDto> teachers = new ArrayList<>(); // Заменен ResponseUserDto
        teachers.add(userMapper.toUserInfoDto(user1)); // Заменен toResponseUserDto
        teachers.add(userMapper.toUserInfoDto(user2)); // Заменен toResponseUserDto
        teachers.add(userMapper.toUserInfoDto(user3)); // Заменен toResponseUserDto
        when(courseUtils.validateAndGetCourseForAuthor(1L)).thenReturn(course);
        when(authorManagementService.getAllTeachersForCourse(1L)).thenReturn(teachers);
        mockMvc.perform(get("/api/v1/author/courses/1/teachers")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    public void testAddTeachersToCourse() throws Exception {
        User user1 = new User();
        user1.setUsername("teacher1");
        user1.setId(1L);
        User user2 = new User();
        user2.setUsername("teacher2");
        user2.setId(2L);
        Set<User> teachersSet = new HashSet<>();
        teachersSet.add(user1);
        teachersSet.add(user2);
        AddOrRemoveTeachersDto addOrRemoveTeachersDto = new AddOrRemoveTeachersDto(Arrays.asList(1L, 2L));
        Course course = new Course();
        course.setId(1L);
        List<UserInfoDto> teachers = new ArrayList<>(); // Заменен ResponseUserDto
        teachers.add(userMapper.toUserInfoDto(user1)); // Заменен toResponseUserDto
        teachers.add(userMapper.toUserInfoDto(user2)); // Заменен toResponseUserDto
        when(userRepo.findByIdIn(addOrRemoveTeachersDto.getIds())).thenReturn(teachersSet);
        when(courseUtils.validateAndGetCourseForAuthor(1L)).thenReturn(course);
        when(authorManagementService.addTeachersForCourse(1L, addOrRemoveTeachersDto)).thenReturn(teachers);

        mockMvc.perform(put("/api/v1/author/courses/1/add-teachers")
                        .with(csrf())
                        .content(new ObjectMapper().writeValueAsString(addOrRemoveTeachersDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testRemoveTeachersToCourse() throws Exception {
        User user1 = new User();
        user1.setUsername("teacher1");
        user1.setId(1L);
        User user2 = new User();
        user2.setUsername("teacher2");
        user2.setId(2L);
        Set<User> teachersSet = new HashSet<>();
        teachersSet.add(user1);
        teachersSet.add(user2);
        AddOrRemoveTeachersDto addOrRemoveTeachersDto = new AddOrRemoveTeachersDto(Arrays.asList(1L, 2L));
        Course course = new Course();
        course.setId(1L);
        List<UserInfoDto> teachers = new ArrayList<>(); // Заменен ResponseUserDto
        teachers.add(userMapper.toUserInfoDto(user1)); // Заменен toResponseUserDto
        teachers.add(userMapper.toUserInfoDto(user2)); // Заменен toResponseUserDto
        when(userRepo.findByIdIn(addOrRemoveTeachersDto.getIds())).thenReturn(teachersSet);
        when(courseUtils.validateAndGetCourseForAuthor(1L)).thenReturn(course);
        when(authorManagementService.removeTeachersForCourse(1L, addOrRemoveTeachersDto)).thenReturn(teachers);

        mockMvc.perform(put("/api/v1/author/courses/1/remove-teachers")
                        .with(csrf())
                        .content(new ObjectMapper().writeValueAsString(addOrRemoveTeachersDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
