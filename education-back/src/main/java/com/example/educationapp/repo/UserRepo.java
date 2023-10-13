package com.example.educationapp.repo;

import com.example.educationapp.dto.response.JournalResponseDto;
import com.example.educationapp.entity.Course;
import com.example.educationapp.entity.Role;
import com.example.educationapp.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Query("select u from User u join u.studentCourseSet c where c =:course")
    Page<User> findByCourse(Course course, Pageable pageable);

    Integer countByStudentCourseSet(Course courses);

    @Query("select u from User u join u.roleSet r where r = :role")
    Page<User> findAllByRole(Role role, Pageable pageable);

    @Query("select c from Course c join c.teachers t where t = :teacher")
    List<Course> findCoursesByTeacher(User teacher);

    @Query("SELECT " +
            "l.id AS lessonId, " +
            "u.id AS studentId, " +
            "CASE " +
            "WHEN COUNT(DISTINCT t) = 0 " +
            "THEN 100.0 " +
            "ELSE (COUNT(DISTINCT hd) * 100.0 / COUNT(DISTINCT t)) " +
            "END AS percentage " +
            "FROM Lesson l " +
            "JOIN l.lessonsCourse c " +
            "JOIN c.students u " +
            "LEFT JOIN HomeworkTask t ON t.lesson = l " +
            "LEFT JOIN HomeworkDone hd ON hd.task = t AND hd.student.id = u.id " +
            "WHERE c = :course AND u IN :students " +
            "GROUP BY l.id, u.id")
    List<Object[]> getHomeworkPercentageForCourse(Course course, List<User> students);


}
