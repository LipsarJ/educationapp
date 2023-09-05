package com.example.educationapp.service;

import com.example.educationapp.dto.response.UserInfoDto;
import com.example.educationapp.entity.User;
import com.example.educationapp.repo.UserRepo;
import com.example.educationapp.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class UserServiceImplTest {

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetUsersWithPaginationAndFilter() {
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("user1");
        user1.setFirstname("John");
        user1.setMiddlename("Doe");
        user1.setLastname("Smith");

        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("user2");
        user2.setFirstname("Jane");
        user2.setMiddlename("Doe");
        user2.setLastname("Johnson");

        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);

        Pageable pageable = PageRequest.of(0, 10);
        Page<User> userPage = new PageImpl<>(users, pageable, users.size());

        when(userRepo.findAll(any(Specification.class), any(Pageable.class))).thenReturn(userPage);

        Page<UserInfoDto> resultPage = userService.getUsersWithPaginationAndFilter("Doe", pageable);

        verify(userRepo, times(1)).findAll(any(Specification.class), any(Pageable.class));

        assert resultPage != null;
        assert resultPage.getContent().size() == 2;

        UserInfoDto firstUserDto = resultPage.getContent().get(0);
        assert firstUserDto.getId() == 1L;
        assert firstUserDto.getUsername().equals("user1");
        assert firstUserDto.getFirstName().equals("John");
        assert firstUserDto.getMiddleName().equals("Doe");
        assert firstUserDto.getLastName().equals("Smith");

        UserInfoDto secondUserDto = resultPage.getContent().get(1);
        assert secondUserDto.getId() == 2L;
        assert secondUserDto.getUsername().equals("user2");
        assert secondUserDto.getFirstName().equals("Jane");
        assert secondUserDto.getMiddleName().equals("Doe");
        assert secondUserDto.getLastName().equals("Johnson");
    }
}
