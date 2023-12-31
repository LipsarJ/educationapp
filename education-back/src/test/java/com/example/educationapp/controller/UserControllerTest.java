package com.example.educationapp.controller;

import com.example.educationapp.dto.response.UserInfoDto;
import com.example.educationapp.service.impl.UserServiceImpl;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@WithMockUser(username = "Lipsar", authorities = "AUTHOR")
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserServiceImpl userService;

    @Test
    public void testGetUsersWithPagination() throws Exception {
        List<UserInfoDto> userInfos = new ArrayList<>();
        userInfos.add(new UserInfoDto(1L, "user1", "John", "Doe", "Smith"));

        Page<UserInfoDto> userPage = new PageImpl<>(userInfos, PageRequest.of(0, 20), userInfos.size());

        when(userService.getUsersWithPaginationAndFilter(any(), any(Pageable.class))).thenReturn(userPage);

        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userInfo").isArray())
                .andExpect(jsonPath("$.userInfo", hasSize(1)))
                .andExpect(jsonPath("$.userInfo[0].id").value(1))
                .andExpect(jsonPath("$.userInfo[0].username").value("user1"))
                .andExpect(jsonPath("$.totalCount").value(userInfos.size()))
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.countValuesPerPage").value(20))
                .andReturn();
    }
}
