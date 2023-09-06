package com.example.educationapp;

import com.example.educationapp.repo.UserRepo;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@ActiveProfiles("test")
@Testcontainers
class EducationappApplicationTests {
    @MockBean
    UserRepo userRepo;

    @Test
    void contextLoads() {
    }

}
