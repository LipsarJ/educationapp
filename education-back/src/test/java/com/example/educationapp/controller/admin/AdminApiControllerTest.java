package com.example.educationapp.controller.admin;

import com.example.educationapp.dto.request.UpdateUserDto;
import com.example.educationapp.dto.request.admin.UpdatePasswordDto;
import com.example.educationapp.dto.response.admin.UserAdminResponseDto;
import com.example.educationapp.entity.User;
import com.example.educationapp.entity.UserStatus;
import com.example.educationapp.mapper.admin.UserAdminMapper;
import com.example.educationapp.repo.UserRepo;
import com.example.educationapp.security.WebSecurityConfig;
import com.example.educationapp.service.admin.AdminApiService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
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
