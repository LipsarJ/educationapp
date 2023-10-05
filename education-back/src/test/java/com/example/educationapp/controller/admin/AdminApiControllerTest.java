package com.example.educationapp.controller.admin;

import com.example.educationapp.dto.request.UpdateUserDto;
import com.example.educationapp.dto.request.admin.UpdatePasswordDto;
import com.example.educationapp.dto.response.ResponseRoleDto;
import com.example.educationapp.dto.response.admin.UserAdminResponseDto;
import com.example.educationapp.entity.ERole;
import com.example.educationapp.entity.UserStatus;
import com.example.educationapp.service.admin.AdminApiService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.time.OffsetDateTime;
import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@WithMockUser(username = "Lipsar", authorities = "ADMIN")
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AdminApiControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AdminApiService adminApiService;

    @Test
    public void testGetUsersWithPaginationAndFilter() throws Exception {
        UserAdminResponseDto user = new UserAdminResponseDto();
        user.setId(1L);
        user.setUsername("testUser");
        user.setEmail("testuser@example.com");

        Pageable pageable = PageRequest.of(0, 10);
        when(adminApiService.getUsersWithPaginationAndFilter(any(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(user), pageable, 1));

        mockMvc.perform(get("/api/v1/admin/user")
                        .param("filterText", "test")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userInfo").isArray())
                .andExpect(jsonPath("$.userInfo", hasSize(1)))
                .andExpect(jsonPath("$.userInfo[0].id").value(1))
                .andExpect(jsonPath("$.userInfo[0].username").value("testUser"))
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.countValuesPerPage").value(10))
                .andReturn();
    }

    @Test
    public void testUpdateUser() throws Exception {
        UpdateUserDto updateUserDto = new UpdateUserDto();
        updateUserDto.setUsername("Updated_Tester");
        updateUserDto.setEmail("updated_test@email.com");
        updateUserDto.setUserStatus(UserStatus.ACTIVE);

        UserAdminResponseDto responseDto = new UserAdminResponseDto();
        responseDto.setId(1L);

        when(adminApiService.updateUser(any(UpdateUserDto.class), anyLong())).thenReturn(responseDto);

        mockMvc.perform(put("/api/v1/admin/user/{id}", 1L).with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(updateUserDto))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value(responseDto.getUsername()))
                .andExpect(jsonPath("$.email").value(responseDto.getEmail()))
                .andExpect(jsonPath("$.userStatus").value(responseDto.getUserStatus()));
    }

    @Test
    public void testUpdateUserPassword() throws Exception {
        UpdatePasswordDto updatePasswordDto = new UpdatePasswordDto();
        updatePasswordDto.setPassword("new_password");

        UserAdminResponseDto responseDto = new UserAdminResponseDto();
        responseDto.setId(1L);

        when(adminApiService.updateUserPassword(any(UpdatePasswordDto.class), anyLong())).thenReturn(responseDto);

        mockMvc.perform(put("/api/v1/admin/user/{id}/password", 1L).with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(updatePasswordDto))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(responseDto.getId()))
                .andExpect(jsonPath("$.username").value(responseDto.getUsername()))
                .andExpect(jsonPath("$.email").value(responseDto.getEmail()));
    }
}
