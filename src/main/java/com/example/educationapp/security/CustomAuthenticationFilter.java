package com.example.educationapp.security;

import com.example.educationapp.entity.User;
import com.example.educationapp.repo.UserRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.http.HttpStatus;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final UserRepo userRepo; // Your JPA repository for User

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            User user = new ObjectMapper().readValue(request.getInputStream(), User.class);
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    user.getUsername(),
                    user.getPassword()
            ));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        User user = userRepo.findByUsername(authResult.getName());

        String token = Jwts.builder()
                .setSubject(user.getUsername())
                .claim("authorities", authResult.getAuthorities())
                .setIssuedAt(new java.util.Date())
                .setExpiration(java.sql.Date.valueOf(java.time.LocalDate.now().plusDays(1)))
                .signWith(Keys.hmacShaKeyFor("your-secret-key".getBytes()))
                .compact();

        response.addHeader("Authorization", "Bearer " + token);
        response.setStatus(HttpStatus.OK.value());
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write("Authentication failed: " + failed.getMessage());
    }
}