package com.example.educationapp.controller.author.management;

import com.example.educationapp.dto.request.authormanagement.AddOrRemoveAuthorsDto;
import com.example.educationapp.dto.response.ResponseUserDto;
import com.example.educationapp.entity.Course;
import com.example.educationapp.entity.User;
import com.example.educationapp.mapper.UserMapper;
import com.example.educationapp.repo.UserRepo;
import com.example.educationapp.security.service.UserContext;
import com.example.educationapp.service.authormanagement.AuthorManagementService;
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
    ResponseUserDto responseUserDto;
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
        List<ResponseUserDto> authors = new ArrayList<>();
        authors.add(userMapper.toDto(user1));
        authors.add(userMapper.toDto(user2));
        when(courseUtils.validateAndGetCourse(1L)).thenReturn(course);
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
        List<ResponseUserDto> authors = new ArrayList<>();
        authors.add(userMapper.toDto(user1));
        authors.add(userMapper.toDto(user2));
        authors.add(userMapper.toDto(user3));
        when(courseUtils.validateAndGetCourse(1L)).thenReturn(course);
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
        List<ResponseUserDto> authors = new ArrayList<>();
        authors.add(userMapper.toDto(user1));
        authors.add(userMapper.toDto(user2));
        when(userRepo.findByIdIn(addOrRemoveAuthorsDto.getIds())).thenReturn(authorsSet);
        when(courseUtils.validateAndGetCourse(1L)).thenReturn(course);
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
        List<ResponseUserDto> authors = new ArrayList<>();
        authors.add(userMapper.toDto(user1));
        authors.add(userMapper.toDto(user2));
        when(userRepo.findByIdIn(addOrRemoveAuthorsDto.getIds())).thenReturn(authorsSet);
        when(courseUtils.validateAndGetCourse(1L)).thenReturn(course);
        when(authorManagementService.addAuthorsForCourse(1L, addOrRemoveAuthorsDto)).thenReturn(authors);

        mockMvc.perform(put("/api/v1/author/courses/1/remove-authors")
                        .with(csrf())
                        .content(new ObjectMapper().writeValueAsString(addOrRemoveAuthorsDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
