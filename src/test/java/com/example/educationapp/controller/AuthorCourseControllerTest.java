package com.example.educationapp.controller;

import com.example.educationapp.dto.CourseDto;
import com.example.educationapp.service.AuthorCourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class AuthorCourseControllerTest {

    @Mock
    private AuthorCourseService authorCourseService;

    private AuthorCourseController authorCourseController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        authorCourseController = new AuthorCourseController(authorCourseService);
    }

    @Test
    public void testGetAllCoursesForAuthor() {
        List<CourseDto> expectedCourses = new ArrayList<>();
        when(authorCourseService.getAllCoursesForAuthor()).thenReturn(expectedCourses);

        List<CourseDto> result = authorCourseController.getAllCoursesForAuthor();

        assertEquals(expectedCourses, result);
        verify(authorCourseService, times(1)).getAllCoursesForAuthor();
    }

    @Test
    public void testCreateCourse() {
        CourseDto courseDto = new CourseDto();
        when(authorCourseService.createCourse(courseDto)).thenReturn(courseDto);

        CourseDto result = authorCourseController.createCourse(courseDto);

        assertNotNull(result);
        assertEquals(courseDto, result);
        verify(authorCourseService, times(1)).createCourse(courseDto);
    }

    @Test
    public void testGetCourse() {
        Long courseId = 1L;
        CourseDto expectedCourseDto = new CourseDto();
        when(authorCourseService.getCourse(courseId)).thenReturn(expectedCourseDto);

        CourseDto result = authorCourseController.getCourse(courseId);

        assertEquals(expectedCourseDto, result);
        verify(authorCourseService, times(1)).getCourse(courseId);
    }

    @Test
    public void testUpdateCourse() {
        Long courseId = 1L;
        CourseDto courseDto = new CourseDto();
        CourseDto updatedCourseDto = new CourseDto();
        when(authorCourseService.updateCourse(courseId, courseDto)).thenReturn(updatedCourseDto);

        CourseDto result = authorCourseController.updateCourse(courseId, courseDto);

        assertEquals(updatedCourseDto, result);
        verify(authorCourseService, times(1)).updateCourse(courseId, courseDto);
    }

    @Test
    public void testDeleteCourse() {
        Long courseId = 1L;

        authorCourseController.deleteCourse(courseId);

        verify(authorCourseService, times(1)).deleteCourse(courseId);
    }
}