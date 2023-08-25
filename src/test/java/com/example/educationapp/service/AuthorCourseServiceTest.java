package com.example.educationapp.service;

import com.example.educationapp.dto.request.RequestCourseDto;
import com.example.educationapp.dto.response.ResponseCourseDto;
import com.example.educationapp.entity.Course;
import com.example.educationapp.entity.CourseStatus;
import com.example.educationapp.entity.Lesson;
import com.example.educationapp.entity.User;
import com.example.educationapp.mapper.CourseMapper;
import com.example.educationapp.repo.CourseRepo;
import com.example.educationapp.repo.LessonRepo;
import com.example.educationapp.repo.UserRepo;
import com.example.educationapp.security.service.UserContext;
import com.example.educationapp.utils.CourseUtils;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AuthorCourseServiceTest {

    private final CourseRepo courseRepo = mock(CourseRepo.class);
    private final UserRepo userRepo = mock(UserRepo.class);
    private final CourseMapper courseMapper = mock(CourseMapper.class);
    private final UserContext userContext = mock(UserContext.class);
    private final CourseUtils courseUtils = mock(CourseUtils.class);
    private final LessonRepo lessonRepo = mock(LessonRepo.class);

    private final AuthorCourseService authorCourseService = new AuthorCourseService(
            courseRepo, userRepo, courseMapper, userContext, courseUtils, lessonRepo
    );

    @Test
    public void testGetAllCoursesForAuthor() {
        // Mock user and courses
        User user = new User();
        List<Course> courses = new ArrayList<>();
        courses.add(new Course());
        courses.add(new Course());

        when(userContext.getUser()).thenReturn(user);
        when(userRepo.findCoursesByAuthorCourseSet(user)).thenReturn(courses);
        when(courseMapper.toResponseDto(any(Course.class))).thenReturn(new ResponseCourseDto());

        List<ResponseCourseDto> responseCourses = authorCourseService.getAllCoursesForAuthor();
        assertEquals(courses.size(), responseCourses.size());
    }

    @Test
    @WithMockUser(username = "Lipsar", roles = "AUTHOR")
    public void testCreateCourse() {
        User user = new User();
        user.setId(1L); // Set the user's ID for testing purposes

        RequestCourseDto requestCourseDto = new RequestCourseDto();
        requestCourseDto.setCourseName("Test Course");
        requestCourseDto.setCourseStatus(CourseStatus.TEMPLATE);

        Course course = new Course();
        course.setId(1L);
        course.setCourseName(requestCourseDto.getCourseName());
        course.setCourseStatus(requestCourseDto.getCourseStatus());

        when(userContext.getUser()).thenReturn(user);
        when(userRepo.findById(user.getId())).thenReturn(Optional.of(user));
        when(courseMapper.toEntity(requestCourseDto)).thenReturn(course);
        when(courseRepo.save(course)).thenReturn(course);
        when(courseMapper.toResponseDto(course)).thenReturn(new ResponseCourseDto(course.getId(), course.getCourseName(), course.getCourseStatus(), OffsetDateTime.now(), OffsetDateTime.now()));

        ResponseCourseDto response = authorCourseService.createCourse(requestCourseDto);

        verify(userRepo).save(user);
        verify(courseRepo).save(course);
        verify(userRepo).save(user);

        assertEquals(course.getId(), response.getId());
        assertEquals(course.getCourseName(), response.getCourseName());
        assertEquals(course.getCourseStatus(), response.getCourseStatus());
    }

    @Test
    public void testGetCourse() {
        Long courseId = 1L;
        ResponseCourseDto responseCourseDto = new ResponseCourseDto();
        when(courseUtils.validateAndGetCourse(courseId)).thenReturn(new Course());
        when(courseMapper.toResponseDto(any(Course.class))).thenReturn(responseCourseDto);

        ResponseCourseDto retrievedCourse = authorCourseService.getCourse(courseId);
        assertEquals(responseCourseDto, retrievedCourse);
    }

    @Test
    public void testUpdateCourse() {
        Long courseId = 1L;
        RequestCourseDto requestCourseDto = new RequestCourseDto();
        ResponseCourseDto responseCourseDto = new ResponseCourseDto();
        Course existingCourse = new Course();

        when(courseUtils.validateAndGetCourse(courseId)).thenReturn(existingCourse);
        when(courseRepo.existsByCourseNameAndIdNot(anyString(), anyLong())).thenReturn(false);
        when(courseUtils.isStatusChangeValid(any(), any())).thenReturn(true);
        when(courseMapper.toResponseDto(any(Course.class))).thenReturn(responseCourseDto);

        ResponseCourseDto updatedCourse = authorCourseService.updateCourse(courseId, requestCourseDto);
        assertEquals(responseCourseDto, updatedCourse);
    }

    @Test
    @WithMockUser(username = "Lipsar", roles = "AUTHOR")
    public void testDeleteCourse() {
        Long courseId = 1L;
        Course course = new Course();
        course.setCourseStatus(CourseStatus.TEMPLATE);
        User user = new User();
        user.setId(1L);

        when(courseUtils.validateAndGetCourse(courseId)).thenReturn(course);
        when(userContext.getUser()).thenReturn(user);
        when(userRepo.findById(user.getId())).thenReturn(Optional.of(user));

        authorCourseService.deleteCourse(courseId);

        verify(userRepo).save(user);
        verify(lessonRepo, times(course.getLessonList().size())).save(any(Lesson.class));
        verify(courseRepo).delete(course);
    }
}
