package com.example.educationapp.service;

import com.example.educationapp.dto.request.RequestLessonDto;
import com.example.educationapp.dto.response.ResponseLessonDto;
import com.example.educationapp.entity.*;
import com.example.educationapp.mapper.LessonMapper;
import com.example.educationapp.repo.CourseRepo;
import com.example.educationapp.repo.HomeworkTaskRepo;
import com.example.educationapp.repo.LessonRepo;
import com.example.educationapp.repo.MediaLessonRepo;
import com.example.educationapp.utils.CourseUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class AuthorLessonServiceTest {

    @Mock
    private LessonRepo lessonRepo;

    @Mock
    private CourseUtils courseUtils;

    @Mock
    private LessonMapper lessonMapper;

    @Mock
    private CourseRepo courseRepo;

    @Mock
    private HomeworkTaskRepo homeworkTaskRepo;

    @Mock
    private MediaLessonRepo mediaLessonRepo;

    @InjectMocks
    private AuthorLessonService authorLessonService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllLessonsWithTwoLessons() {
        Long courseId = 1L;
        Course course = new Course();
        List<Lesson> lessons = new ArrayList<>();
        lessons.add(new Lesson());
        lessons.add(new Lesson());

        when(courseUtils.validateAndGetCourseForAuthor(courseId)).thenReturn(course);
        when(lessonRepo.findAllByLessonsCourse(course)).thenReturn(lessons);
        when(lessonMapper.toResponseDto(any(Lesson.class))).thenReturn(new ResponseLessonDto());

        List<ResponseLessonDto> responseLessonDtos = authorLessonService.getAllLessons(courseId);

        assertEquals(lessons.size(), responseLessonDtos.size());
        verify(courseUtils).validateAndGetCourseForAuthor(courseId);
        verify(lessonRepo).findAllByLessonsCourse(course);
        verify(lessonMapper, times(lessons.size())).toResponseDto(any(Lesson.class));
    }

    @Test
    void testGetAllLessonsWithThreeLessons() {
        Long courseId = 1L;
        Course course = new Course();
        List<Lesson> lessons = new ArrayList<>();
        lessons.add(new Lesson());
        lessons.add(new Lesson());
        lessons.add(new Lesson());

        when(courseUtils.validateAndGetCourseForAuthor(courseId)).thenReturn(course);
        when(lessonRepo.findAllByLessonsCourse(course)).thenReturn(lessons);
        when(lessonMapper.toResponseDto(any(Lesson.class))).thenReturn(new ResponseLessonDto());

        List<ResponseLessonDto> responseLessonDtos = authorLessonService.getAllLessons(courseId);

        assertEquals(lessons.size(), responseLessonDtos.size());
        verify(courseUtils).validateAndGetCourseForAuthor(courseId);
        verify(lessonRepo).findAllByLessonsCourse(course);
        verify(lessonMapper, times(lessons.size())).toResponseDto(any(Lesson.class));
    }

    @Test
    void testCreateLesson() {
        Long courseId = 1L;
        RequestLessonDto requestLessonDto = new RequestLessonDto();
        requestLessonDto.setLessonName("Lesson1");
        Course course = new Course();
        when(courseUtils.validateAndGetCourseForAuthor(courseId)).thenReturn(course);
        when(lessonRepo.existsByLessonName(requestLessonDto.getLessonName())).thenReturn(false);
        when(lessonMapper.toEntity(requestLessonDto)).thenReturn(new Lesson());
        when(lessonRepo.save(any(Lesson.class))).thenReturn(new Lesson());
        when(lessonMapper.toResponseDto(any(Lesson.class))).thenReturn(new ResponseLessonDto());

        ResponseLessonDto responseLessonDto = authorLessonService.createLesson(courseId, requestLessonDto);

        assertNotNull(responseLessonDto);
        verify(courseUtils).validateAndGetCourseForAuthor(courseId);
        verify(lessonRepo).existsByLessonName(requestLessonDto.getLessonName());
        verify(lessonMapper).toEntity(requestLessonDto);
        verify(lessonRepo).save(any(Lesson.class));
        verify(lessonMapper).toResponseDto(any(Lesson.class));
    }

    @Test
    void testGetLesson() {
        Long courseId = 1L;
        Long lessonId = 2L;
        Course course = new Course();
        Lesson lesson = new Lesson();
        when(courseUtils.validateAndGetCourseForAuthor(courseId)).thenReturn(course);
        when(lessonRepo.findById(lessonId)).thenReturn(Optional.of(lesson));
        when(lessonMapper.toResponseDto(lesson)).thenReturn(new ResponseLessonDto());

        ResponseLessonDto responseLessonDto = authorLessonService.getLesson(courseId, lessonId);

        assertNotNull(responseLessonDto);
        verify(courseUtils).validateAndGetCourseForAuthor(courseId);
        verify(lessonRepo).findById(lessonId);
        verify(lessonMapper).toResponseDto(lesson);
    }

    @Test
    void testUpdateLesson() {
        Long courseId = 1L;
        Long lessonId = 2L;
        RequestLessonDto requestLessonDto = new RequestLessonDto();
        requestLessonDto.setLessonName("Lesson1");
        requestLessonDto.setLessonStatus(LessonStatus.ACTIVE);
        Lesson lesson = new Lesson();

        when(lessonRepo.existsByLessonNameAndIdNot(requestLessonDto.getLessonName(), lessonId)).thenReturn(false);
        when(lessonRepo.findByLessonName(eq(requestLessonDto.getLessonName()))).thenReturn(null);
        when(lessonRepo.findById(lessonId)).thenReturn(Optional.of(lesson));
        when(lessonMapper.toResponseDto(lesson)).thenReturn(new ResponseLessonDto());

        ResponseLessonDto responseLessonDto = authorLessonService.updateLesson(courseId, lessonId, requestLessonDto);

        assertNotNull(responseLessonDto);
        verify(courseUtils).validateAndGetCourseForAuthor(courseId);
        verify(lessonRepo).existsByLessonNameAndIdNot(requestLessonDto.getLessonName(), lessonId);
        verify(lessonRepo).findById(lessonId);
        verify(lessonRepo).save(any(Lesson.class));
        verify(lessonMapper).toResponseDto(lesson);
    }

    @Test
    void testDeleteLesson() {
        Long courseId = 1L;
        Long lessonId = 2L;
        Course course = new Course();
        Lesson lesson = new Lesson();
        lesson.setLessonStatus(LessonStatus.NOT_ACTIVE);
        List<HomeworkTask> homeworkTasks = new ArrayList<>();
        homeworkTasks.add(new HomeworkTask());
        lesson.setHomeworkTaskList(homeworkTasks);
        List<MediaLesson> mediaLessons = new ArrayList<>();
        mediaLessons.add(new MediaLesson());
        lesson.setMediaLessonList(mediaLessons);
        when(courseUtils.validateAndGetCourseForAuthor(courseId)).thenReturn(course);
        when(lessonRepo.findById(lessonId)).thenReturn(Optional.of(lesson));

        lesson.setLessonStatus(LessonStatus.NOT_ACTIVE);
        lesson.setHomeworkTaskList(new ArrayList<>());
        lesson.setMediaLessonList(new ArrayList<>());

        authorLessonService.deleteLesson(courseId, lessonId);

        verify(courseUtils, times(1)).validateAndGetCourseForAuthor(courseId);
        verify(lessonRepo).findById(lessonId);
        verify(homeworkTaskRepo, times(0)).save(any(HomeworkTask.class));
        verify(mediaLessonRepo, times(0)).save(any(MediaLesson.class));
        verify(courseRepo).save(course);
        verify(lessonRepo).delete(lesson);
    }
}