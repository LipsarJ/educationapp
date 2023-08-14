package com.example.educationapp.repo;

import com.example.educationapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByUsername (String username);
}
