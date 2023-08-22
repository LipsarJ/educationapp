package com.example.educationapp.service;

import com.example.educationapp.dto.request.RequestCourseDto;
import com.example.educationapp.dto.response.ResponseCourseDto;
import com.example.educationapp.entity.*;
import com.example.educationapp.mapper.CourseMapper;
import com.example.educationapp.repo.CourseRepo;
import com.example.educationapp.repo.RoleRepo;
import com.example.educationapp.repo.UserRepo;
import com.example.educationapp.security.service.UserContext;
import com.example.educationapp.utils.CourseUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
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

        ResponseCourseDto responseCourseDto = createResponseCourseDto();

        when(userContext.getUser()).thenReturn(user);
        when(userRepo.findCoursesByAuthorCourseSet(any(User.class))).thenReturn(Collections.singletonList(course));
        when(courseMapper.toResponseDto(course)).thenReturn(responseCourseDto);

        // Вызываем метод, который хотим протестировать
        List<ResponseCourseDto> courses = authorCourseService.getAllCoursesForAuthor();

        // Проверяем результат
        assertNotNull(courses);
        assertEquals(1, courses.size());
        // Проверьте, что методы моков были вызваны верно
        verify(userContext, times(1)).getUser();
        verify(userRepo, times(1)).findCoursesByAuthorCourseSet(any(User.class));
        verify(courseMapper, times(1)).toResponseDto(course);
    }

    @Test
    public void testCreateCourse() {
        RequestCourseDto requestCourseDto = createRequestCourseDto();
        ResponseCourseDto responseCourseDto = createResponseCourseDto();
        when(courseMapper.toEntity(any(RequestCourseDto.class))).thenReturn(createCourseEntity());
        when(courseRepo.save(any())).thenReturn(createCourseEntity());
        when(courseMapper.toResponseDto(any())).thenReturn(responseCourseDto);

        ResponseCourseDto createdCourse = authorCourseService.createCourse(requestCourseDto);

        assertNotNull(createdCourse);
        assertEquals(requestCourseDto.getCourseName(), createdCourse.getCourseName());
        assertEquals(requestCourseDto.getCourseStatus(), createdCourse.getCourseStatus());
    }

    @Test
    public void testGetCourse() {
        ResponseCourseDto responseCourseDto = createResponseCourseDto();
        RequestCourseDto requestCourseDto = createRequestCourseDto();
        when(courseUtils.validateAndGetCourse(anyLong())).thenReturn(createCourseEntity());
        when(courseMapper.toResponseDto(any())).thenReturn(responseCourseDto);

        ResponseCourseDto retrievedCourse = authorCourseService.getCourse(1L);

        assertNotNull(retrievedCourse);
        assertEquals(requestCourseDto.getCourseName(), retrievedCourse.getCourseName());
        assertEquals(requestCourseDto.getCourseStatus(), retrievedCourse.getCourseStatus());
    }

    @Test
    public void testUpdateCourse() {
        RequestCourseDto requestCourseDto = createRequestCourseDto();
        ResponseCourseDto responseCourseDto = createResponseCourseDto();
        when(courseUtils.validateAndGetCourse(anyLong())).thenReturn(createCourseEntity());
        when(courseUtils.isStatusChangeValid(any(), any())).thenReturn(true);
        when(courseRepo.save(any())).thenReturn(createCourseEntity());
        when(courseMapper.toResponseDto(any())).thenReturn(responseCourseDto);

        ResponseCourseDto updatedCourse = authorCourseService.updateCourse(1L, requestCourseDto);

        assertNotNull(updatedCourse);
        assertEquals(requestCourseDto.getCourseName(), updatedCourse.getCourseName());
        assertEquals(requestCourseDto.getCourseStatus(), updatedCourse.getCourseStatus());
    }

    @Test
    public void testDeleteCourse() {
        Long courseId = 1L;
        Course courseEntity = createCourseEntity();

        when(courseUtils.validateAndGetCourse(courseId)).thenReturn(courseEntity);
        doNothing().when(courseRepo).delete(courseEntity);

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

    private RequestCourseDto createRequestCourseDto() {
        RequestCourseDto requestCourseDto = new RequestCourseDto();
        requestCourseDto.setCourseName("Introduction to Programming");
        requestCourseDto.setCourseStatus(CourseStatus.TEMPLATE);
        return requestCourseDto;
    }

    private ResponseCourseDto createResponseCourseDto() {
        ResponseCourseDto responseCourseDto = new ResponseCourseDto();
        responseCourseDto.setId(1L);
        responseCourseDto.setCourseName("Introduction to Programming");
        responseCourseDto.setCourseStatus(CourseStatus.TEMPLATE);
        responseCourseDto.setCreateDate(OffsetDateTime.now());
        responseCourseDto.setUpdateDate(OffsetDateTime.now());
        return responseCourseDto;
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