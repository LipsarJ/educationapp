package com.example.educationapp.repo;

import com.example.educationapp.entity.Course;
import com.example.educationapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    List<Course> findCoursesByAuthorCourseSet(User author);
}
