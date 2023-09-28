package com.example.educationapp.repo;

import com.example.educationapp.entity.Course;
import com.example.educationapp.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserRepo extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmailIgnoreCase(String email);

    @Query("select c from Course c join c.authors a where a = :author")
    List<Course> findCoursesByAuthor(User author);

    boolean existsByUsernameAndIdNot(String username, Long userId);

    boolean existsByEmailAndIdNot(String email, Long userId);

    Set<User> findByIdIn(List<Long> userIds);

    @Query("select s from User s join s.studentCourseSet c where c =:course")
    Page<User> findByCourse(Course course, Pageable pageable);
}
