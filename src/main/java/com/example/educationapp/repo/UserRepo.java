package com.example.educationapp.repo;

import com.example.educationapp.entity.Course;
import com.example.educationapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    @Query("select c from Course c join c.authors a where a = :author")
    List<Course> findCoursesByAuthor(User author);

    boolean existsByUsernameAndIdNot(String username, Long userId);

    boolean existsByEmailAndIdNot(String email, Long userId);
}
