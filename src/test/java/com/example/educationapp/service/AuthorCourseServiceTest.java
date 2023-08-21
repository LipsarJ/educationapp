package com.example.educationapp.service;

import com.example.educationapp.dto.CourseDto;
import com.example.educationapp.entity.*;
import com.example.educationapp.exception.InvalidStatusException;
import com.example.educationapp.mapper.CourseMapper;
import com.example.educationapp.repo.CourseRepo;
import com.example.educationapp.repo.RoleRepo;
import com.example.educationapp.repo.UserRepo;
import com.example.educationapp.security.service.UserContext;
import com.example.educationapp.service.AuthorCourseService;
import com.example.educationapp.utils.CourseUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class AuthorCourseServiceTest {

    @Mock
    private CourseRepo courseRepo;

    @Mock
    private RoleRepo roleRepo;

    @Mock
    private UserRepo userRepo;

    @Mock
    private UserContext userContext;

    @Mock
    private CourseUtils courseUtils;

    @Mock
    private CourseMapper courseMapper;

    @InjectMocks
    private AuthorCourseService authorCourseService;

    @BeforeEach
    public void setUp() {
        Role role = new Role();
        MockitoAnnotations.initMocks(this);
        when(roleRepo.findById(2L)).thenReturn(Optional.of(role));
    }

    @Test
    void testGetAllCoursesForAuthor() {
        User user = createUser();
        Course course = createCourseEntity();

        when(userContext.getUser()).thenReturn(user);
        when(userRepo.findCoursesByAuthorCourseSet(any(User.class))).thenReturn(Collections.singletonList(course));
        when(courseMapper.toDto(course)).thenReturn(createCourseDto());

        // Вызываем метод, который хотим протестировать
        List<CourseDto> courses = authorCourseService.getAllCoursesForAuthor();

        // Проверяем результат
        assertNotNull(courses);
        assertEquals(1, courses.size());
        // Проверьте, что методы моков были вызваны верно
        verify(userContext, times(1)).getUser();
        verify(userRepo, times(1)).findCoursesByAuthorCourseSet(any(User.class));
        verify(courseMapper, times(1)).toDto(course);
    }

    @Test
    public void testCreateCourse() {
        CourseDto courseDto = createCourseDto();
        when(courseMapper.toEntity(any(CourseDto.class))).thenReturn(createCourseEntity());
        when(courseRepo.save(any())).thenReturn(createCourseEntity());
        when(courseMapper.toDto(any())).thenReturn(courseDto);

        CourseDto createdCourse = authorCourseService.createCourse(courseDto);

        assertNotNull(createdCourse);
        assertEquals(courseDto.getId(), createdCourse.getId());
        assertEquals(courseDto.getCourseName(), createdCourse.getCourseName());
        assertEquals(courseDto.getStatus(), createdCourse.getStatus());
        assertEquals(courseDto.getCreateDate(), createdCourse.getCreateDate());
        assertEquals(courseDto.getUpdateDate(), createdCourse.getUpdateDate());
    }

    @Test
    public void testGetCourse() {
        CourseDto courseDto = createCourseDto();
        when(courseUtils.validateAndGetCourse(anyLong())).thenReturn(createCourseEntity());
        when(courseMapper.toDto(any())).thenReturn(courseDto);

        CourseDto retrievedCourse = authorCourseService.getCourse(1L);

        assertNotNull(retrievedCourse);
        assertEquals(courseDto.getId(), retrievedCourse.getId());
        assertEquals(courseDto.getCourseName(), retrievedCourse.getCourseName());
        assertEquals(courseDto.getStatus(), retrievedCourse.getStatus());
        assertEquals(courseDto.getCreateDate(), retrievedCourse.getCreateDate());
        assertEquals(courseDto.getUpdateDate(), retrievedCourse.getUpdateDate());
    }

    @Test
    public void testUpdateCourse() {
        CourseDto courseDto = createCourseDto();
        when(courseUtils.validateAndGetCourse(anyLong())).thenReturn(createCourseEntity());
        when(courseUtils.isStatusChangeValid(any(), any())).thenReturn(true);
        when(courseRepo.save(any())).thenReturn(createCourseEntity());
        when(courseMapper.toDto(any())).thenReturn(courseDto);

        CourseDto updatedCourse = authorCourseService.updateCourse(1L, courseDto);

        assertNotNull(updatedCourse);
        assertEquals(courseDto.getId(), updatedCourse.getId());
        assertEquals(courseDto.getCourseName(), updatedCourse.getCourseName());
        assertEquals(courseDto.getStatus(), updatedCourse.getStatus());
        assertEquals(courseDto.getCreateDate(), updatedCourse.getCreateDate());
        assertEquals(courseDto.getUpdateDate(), updatedCourse.getUpdateDate());
    }

    @Test
    public void testDeleteCourse() {
        // Arrange
        Long courseId = 1L;
        Course courseEntity = createCourseEntity();

        when(courseUtils.validateAndGetCourse(courseId)).thenReturn(courseEntity);
        doNothing().when(courseRepo).delete(courseEntity);

        // Act & Assert
        assertDoesNotThrow(() -> authorCourseService.deleteCourse(courseId));

        verify(courseUtils, times(1)).validateAndGetCourse(courseId);
        verify(courseRepo, times(1)).delete(courseEntity);
    }

    private User createUser() {
        Role role = roleRepo.findById(2L).orElseThrow();
        User user = new User(
                1L,                     // id
                "authorUser",             // username
                "email@example.com",    // email
                "Lastname",             // lastname
                "Middlename",           // middlename
                "Firstname",            // firstname
                "password",             // password
                UserStatus.PENDING,      // status
                LocalDateTime.now(),    // createDate
                LocalDateTime.now(),    // updateDate
                new HashSet<>(Collections.singletonList(role)),        // roleSet
                new HashSet<>(),        // studentCourseSet
                new HashSet<>(),        // authorCourseSet
                new HashSet<>()         // teacherCourseSet
        );
        return user;
    }

    private CourseDto createCourseDto() {
        CourseDto courseDto = new CourseDto();
        courseDto.setId(1L);
        courseDto.setCourseName("Introduction to Programming");
        courseDto.setStatus(CourseStatus.TEMPLATE);
        courseDto.setCreateDate(OffsetDateTime.now().minusDays(30));
        courseDto.setUpdateDate(OffsetDateTime.now().minusDays(10));
        return courseDto;
    }

    private Course createCourseEntity() {
        Course course = new Course();
        course.setId(1L);
        course.setCourseName("Introduction to Programming");
        course.setStatus(CourseStatus.TEMPLATE);
        course.setCreateDate(LocalDateTime.now().minusDays(30));
        course.setUpdateDate(LocalDateTime.now().minusDays(10));
        return course;
    }
}