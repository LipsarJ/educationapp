package com.example.educationapp.service;

import com.example.educationapp.dto.request.RequestLessonDto;
import com.example.educationapp.dto.response.ResponseLessonDto;
import com.example.educationapp.entity.Course;
import com.example.educationapp.entity.CourseStatus;
import com.example.educationapp.entity.Lesson;
import com.example.educationapp.entity.LessonStatus;
import com.example.educationapp.exception.InvalidStatusException;
import com.example.educationapp.exception.LessonNotFoundException;
import com.example.educationapp.mapper.LessonMapper;
import com.example.educationapp.repo.LessonRepo;
import com.example.educationapp.utils.CourseUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class AuthorLessonServiceTest {

    private AuthorLessonService authorLessonService;

    @Mock
    private LessonRepo lessonRepo;

    @Mock
    private CourseUtils courseUtils;

    @Mock
    private LessonMapper lessonMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        authorLessonService = new AuthorLessonService(lessonRepo, courseUtils, lessonMapper);
    }

    @Test
    public void testGetAllLessons() {
        // Arrange
        Long courseId = 1L;
        List<Lesson> lessonEntities = new ArrayList<>();
        lessonEntities.add(createUpdatedLesson());
        List<ResponseLessonDto> expectedLessons = new ArrayList<>();

        expectedLessons.add(createResponseLessonDto());

        Course course = new Course();

        when(courseUtils.validateAndGetCourse(courseId)).thenReturn(course);
        when(lessonRepo.findAllByLessonsCourse(course)).thenReturn(lessonEntities);
        when(lessonMapper.toResponseDto(any(Lesson.class))).thenReturn(new ResponseLessonDto());

        List<ResponseLessonDto> result = authorLessonService.getAllLessons(courseId);

        assertNotNull(result);
        assertEquals(expectedLessons.size(), result.size());
    }

    @Test
    public void testCreateLesson() {
        Long courseId = 1L;
        RequestLessonDto requestLessonDto = createRequestLessonDto();

        Course course = new Course();
        Lesson createdLesson = new Lesson();
        ResponseLessonDto responseLessonDto = createResponseLessonDto();

        when(courseUtils.validateAndGetCourse(courseId)).thenReturn(course);
        when(lessonMapper.toEntity(requestLessonDto)).thenReturn(createdLesson);
        when(lessonRepo.save(createdLesson)).thenReturn(createdLesson);
        when(lessonMapper.toResponseDto(createdLesson)).thenReturn(responseLessonDto);

        ResponseLessonDto result = authorLessonService.createLesson(courseId, requestLessonDto);

        assertNotNull(result);
        assertEquals(responseLessonDto, result);
    }

    @Test
    public void testGetLesson() {
        Long courseId = 1L;
        Long id = 1L;

        Course course = new Course();
        Lesson lesson = createUpdatedLesson();

        when(courseUtils.validateAndGetCourse(courseId)).thenReturn(course);
        when(lessonRepo.findById(id)).thenReturn(java.util.Optional.of(lesson));
        when(lessonMapper.toResponseDto(lesson)).thenReturn(new ResponseLessonDto());

        ResponseLessonDto result = authorLessonService.getLesson(courseId, id);

        assertNotNull(result);
    }

    @Test
    public void testUpdateLesson() {
        Long courseId = 1L;
        Long id = 1L;
        RequestLessonDto requestLessonDto = createRequestLessonDto();

        Course course = new Course();
        Lesson updatedLesson = createUpdatedLesson();
        ResponseLessonDto responseLessonDto = createResponseLessonDto();

        requestLessonDto.setLessonStatus(LessonStatus.ACTIVE);

        when(courseUtils.validateAndGetCourse(courseId)).thenReturn(course);
        when(lessonMapper.toEntity(requestLessonDto)).thenReturn(updatedLesson);
        when(lessonRepo.save(updatedLesson)).thenReturn(updatedLesson);
        when(lessonMapper.toResponseDto(updatedLesson)).thenReturn(responseLessonDto);

        ResponseLessonDto result = authorLessonService.updateLesson(courseId, id, requestLessonDto);

        assertNotNull(result);
        assertEquals(LessonStatus.ACTIVE, result.getLessonStatus());
    }

    @Test
    public void testDeleteLesson() {
        Long courseId = 1L;
        Long id = 1L;

        Lesson lesson = new Lesson();
        lesson.setStatus(LessonStatus.NOT_ACTIVE);

        when(courseUtils.validateAndGetCourse(courseId)).thenReturn(new Course());
        when(lessonRepo.findById(id)).thenReturn(Optional.of(lesson));

        assertDoesNotThrow(() -> authorLessonService.deleteLesson(courseId, id));

        verify(courseUtils, times(1)).validateAndGetCourse(courseId);
        verify(lessonRepo, times(1)).findById(id);
        verify(lessonRepo, times(1)).delete(lesson);
    }

    private Lesson createUpdatedLesson() {
        Lesson updatedLesson = new Lesson();
        updatedLesson.setLessonName("Lesson");
        updatedLesson.setContent("content");
        updatedLesson.setStatus(LessonStatus.NOT_ACTIVE);
        return updatedLesson;
    }

    private ResponseLessonDto createResponseLessonDto() {
        ResponseLessonDto responseLessonDto = new ResponseLessonDto();
        responseLessonDto.setLessonName("Lesson 1");
        responseLessonDto.setContent("Content of Lesson 1");
        responseLessonDto.setLessonStatus(LessonStatus.ACTIVE);
        return responseLessonDto;
    }

    private RequestLessonDto createRequestLessonDto() {
        RequestLessonDto requestLessonDto = new RequestLessonDto();
        requestLessonDto.setId(1L);
        requestLessonDto.setLessonName("Lesson 1");
        requestLessonDto.setContent("Content of Lesson 1");
        requestLessonDto.setLessonStatus(LessonStatus.ACTIVE);
        return requestLessonDto;
    }
}