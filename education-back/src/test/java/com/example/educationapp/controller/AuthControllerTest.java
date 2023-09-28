package com.example.educationapp.controller;

import com.example.educationapp.dto.request.LoginDto;
import com.example.educationapp.dto.request.SignupDto;
import com.example.educationapp.entity.RefreshToken;
import com.example.educationapp.entity.User;
import com.example.educationapp.exception.BadDataException;
import com.example.educationapp.repo.RefreshTokenRepo;
import com.example.educationapp.repo.UserRepo;
import com.example.educationapp.security.WebSecurityConfig;
import com.example.educationapp.security.jwt.JwtUtils;
import com.example.educationapp.security.service.RefreshTokenService;
import com.example.educationapp.security.service.UserDetailsImpl;
import com.example.educationapp.security.service.UserDetailsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc
@Import(WebSecurityConfig.class)
public class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserRepo userRepo;
    @MockBean
    UserDetailsServiceImpl userDetailsService;
    @MockBean
    private JwtUtils jwtUtils;
    @MockBean
    private RefreshTokenService refreshTokenService;
    @MockBean
    private PasswordEncoder passwordEncoder;
    @MockBean
    private AuthenticationManager authenticationManager;
    @MockBean
    private RefreshTokenRepo refreshTokenRepo;
    @Mock
    private Authentication authentication;

    @Test
    public void testAuthenticateUser() throws Exception {
        LoginDto loginDto = new LoginDto("testuser", "testpassword");
        Authentication authentication = mock(Authentication.class);
        UserDetailsImpl userDetails = new UserDetailsImpl(1L, "testuser", "test@test.com", "testpassword", new ArrayList<>());
        ResponseCookie jwtCookie = ResponseCookie.from("jwtCookie", "jwtToken").httpOnly(true).path("/").build();
        ResponseCookie jwtRefreshCookie = ResponseCookie.from("jwtRefreshCookie", "refreshToken").httpOnly(true).path("/").build();
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken("ksahjh3412");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtUtils.generateRefreshJwtCookie(refreshToken.getToken()))
                .thenReturn(jwtRefreshCookie);
        when(jwtUtils.generateJwtCookie(userDetails)).thenReturn(jwtCookie);
        when(refreshTokenService.createRefreshToken(1L)).thenReturn(refreshToken);

        mockMvc.perform(post("/api/v1/auth/signin")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("test@test.com"));
    }

    @Test
    public void testRegisterUser() throws Exception {
        SignupDto signupDto = new SignupDto("newuser", "new@test.com", "newpassword", "Middle", "First", "Last");

        when(userRepo.existsByUsername("newuser")).thenReturn(false);
        when(userRepo.existsByEmailIgnoreCase("new@test.com")).thenReturn(false);
        when(jwtUtils.generateJwtCookie(any(UserDetailsImpl.class))).thenReturn(ResponseCookie.from("jwtCookie", "jwtToken").httpOnly(true).path("/").build());

        mockMvc.perform(post("/api/v1/auth/signup")
                        .with(csrf())
                        .with(user("newuser"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(signupDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User registered successfully!"));
    }

    @Test
    public void testLogoutUser() throws Exception {
        when(jwtUtils.getCleanJwtCookie()).thenReturn(ResponseCookie.from("jwtCookie", "").httpOnly(true).path("/").build());
        when(jwtUtils.getCleanJwtRefreshCookie()).thenReturn(ResponseCookie.from("jwtRefreshCookie", "").httpOnly(true).path("/").build());

        mockMvc.perform(post("/api/v1/auth/signout")
                        .with(user("lipsar"))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("You've been signed out!"));
    }

    @Test
    public void testRefreshToken() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken("jfkdasd");
        refreshToken.setUser(user);


        when(jwtUtils.getJwtRefreshFromCookies(any())).thenReturn("jfkdasd");
        when(refreshTokenRepo.findByToken("jfkdasd")).thenReturn(Optional.of(refreshToken));
        when(refreshTokenService.verifyExpiration(refreshToken)).thenReturn(refreshToken);
        ResponseCookie jwtCookie = ResponseCookie.from("jwtCookie", "newJwtToken")
                .httpOnly(true)
                .path("/")
                .build();
        when(jwtUtils.generateJwtCookie(user)).thenReturn(jwtCookie);

        mockMvc.perform(post("/api/v1/auth/refreshtoken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user("testuser"))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.SET_COOKIE, jwtCookie.toString()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Token is refreshed successfully!"));
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new BadDataException(e.getMessage());
        }
    }
}
