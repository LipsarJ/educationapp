package com.example.educationapp.service;

import com.example.educationapp.dto.request.RequestHomeworkTaskDto;
import com.example.educationapp.dto.response.ResponseHomeworkTaskDto;
import com.example.educationapp.entity.Course;
import com.example.educationapp.entity.HomeworkTask;
import com.example.educationapp.entity.Lesson;
import com.example.educationapp.mapper.HomeworkTaskMapper;
import com.example.educationapp.repo.HomeworkTaskRepo;
import com.example.educationapp.repo.LessonRepo;
import com.example.educationapp.utils.CourseUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthorHomeworkTaskServiceTest {

    @Mock
    private HomeworkTaskRepo homeworkTaskRepo;

    @Mock
    private HomeworkTaskMapper homeworkTaskMapper;

    @Mock
    private CourseUtils courseUtils;

    @Mock
    private LessonRepo lessonRepo;

    @InjectMocks
    private AuthorHomeworkTaskService authorHomeworkTaskService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetAllTasks() {
        Long courseId = 1L;
        Long lessonId = 2L;

        Lesson lesson = new Lesson();
        lesson.setId(lessonId);

        HomeworkTask homeworkTask = new HomeworkTask();
        ResponseHomeworkTaskDto responseHomeworkTaskDto = createResponseHomeworkTaskDto();
        Course course = new Course();
        course.setId(courseId);

        when(courseUtils.validateAndGetCourse(courseId)).thenReturn(course);
        when(lessonRepo.findById(lessonId)).thenReturn(Optional.of(lesson));
        when(homeworkTaskRepo.findAllByLesson(lesson)).thenReturn(Collections.singletonList(homeworkTask));
        when(homeworkTaskMapper.toResponseDto(homeworkTask)).thenReturn(responseHomeworkTaskDto);

        List<ResponseHomeworkTaskDto> tasks = authorHomeworkTaskService.getAllTasks(courseId, lessonId);

        assertNotNull(tasks);
        assertEquals(1, tasks.size());
        assertEquals(responseHomeworkTaskDto, tasks.get(0));
        verify(courseUtils, times(1)).validateAndGetCourse(courseId);
        verify(lessonRepo, times(1)).findById(lessonId);
        verify(homeworkTaskMapper, times(1)).toResponseDto(homeworkTask);
    }

    @Test
    public void testCreateTask() {
        Long courseId = 1L;
        Long lessonId = 2L;

        Lesson lesson = new Lesson();
        lesson.setId(lessonId);

        RequestHomeworkTaskDto requestHomeworkTaskDto = new RequestHomeworkTaskDto();
        HomeworkTask homeworkTask = new HomeworkTask();
        ResponseHomeworkTaskDto responseHomeworkTaskDto = createResponseHomeworkTaskDto();

        Course course = new Course();

        when(courseUtils.validateAndGetCourse(courseId)).thenReturn(course);
        when(lessonRepo.findById(lessonId)).thenReturn(Optional.of(lesson));
        when(homeworkTaskMapper.toEntity(requestHomeworkTaskDto)).thenReturn(homeworkTask);
        when(homeworkTaskRepo.save(homeworkTask)).thenReturn(homeworkTask);
        when(homeworkTaskMapper.toResponseDto(homeworkTask)).thenReturn(responseHomeworkTaskDto);

        ResponseHomeworkTaskDto createdTask = authorHomeworkTaskService.createTask(courseId, lessonId, requestHomeworkTaskDto);

        assertNotNull(createdTask);
        assertEquals(responseHomeworkTaskDto, createdTask);
        verify(courseUtils, times(1)).validateAndGetCourse(courseId);
        verify(lessonRepo, times(1)).findById(lessonId);
        verify(homeworkTaskMapper, times(1)).toEntity(requestHomeworkTaskDto);
        verify(homeworkTaskRepo, times(1)).save(homeworkTask);
        verify(homeworkTaskMapper, times(1)).toResponseDto(homeworkTask);
    }

    @Test
    public void testGetTask() {
        Long courseId = 1L;
        Long lessonId = 2L;
        Long taskId = 3L;

        HomeworkTask homeworkTask = new HomeworkTask();
        ResponseHomeworkTaskDto responseHomeworkTaskDto = createResponseHomeworkTaskDto();

        Course course = new Course();

        when(courseUtils.validateAndGetCourse(courseId)).thenReturn(course);
        when(homeworkTaskRepo.findById(taskId)).thenReturn(Optional.of(homeworkTask));
        when(homeworkTaskMapper.toResponseDto(homeworkTask)).thenReturn(responseHomeworkTaskDto);

        ResponseHomeworkTaskDto retrievedTask = authorHomeworkTaskService.getTask(courseId, lessonId, taskId);

        assertNotNull(retrievedTask);
        assertEquals(responseHomeworkTaskDto, retrievedTask);
        verify(courseUtils, times(1)).validateAndGetCourse(courseId);
        verify(homeworkTaskRepo, times(1)).findById(taskId);
        verify(homeworkTaskMapper, times(1)).toResponseDto(homeworkTask);
    }

    @Test
    public void testUpdateTask() {
        Long courseId = 1L;
        Long lessonId = 2L;
        Long taskId = 3L;

        RequestHomeworkTaskDto requestHomeworkTaskDto = new RequestHomeworkTaskDto();

        HomeworkTask homeworkTask = new HomeworkTask();
        ResponseHomeworkTaskDto responseHomeworkTaskDto = createResponseHomeworkTaskDto();

        Course course = new Course();

        when(courseUtils.validateAndGetCourse(courseId)).thenReturn(course);
        when(homeworkTaskRepo.save(homeworkTask)).thenReturn(homeworkTask);
        when(homeworkTaskMapper.toEntity(requestHomeworkTaskDto)).thenReturn(homeworkTask);
        when(homeworkTaskMapper.toResponseDto(homeworkTask)).thenReturn(responseHomeworkTaskDto);

        ResponseHomeworkTaskDto updatedTask = authorHomeworkTaskService.updateTask(courseId, lessonId, taskId, requestHomeworkTaskDto);

        assertNotNull(updatedTask);
        assertEquals(responseHomeworkTaskDto, updatedTask);
        verify(courseUtils, times(1)).validateAndGetCourse(courseId);
        verify(homeworkTaskRepo, times(1)).save(homeworkTask);
        verify(homeworkTaskMapper, times(1)).toEntity(requestHomeworkTaskDto);
        verify(homeworkTaskMapper, times(1)).toResponseDto(homeworkTask);
    }

    @Test
    public void testDeleteTask() {
        Long courseId = 1L;
        Long lessonId = 2L;
        Long taskId = 3L;

        HomeworkTask homeworkTask = new HomeworkTask();

        Course course = new Course();

        when(courseUtils.validateAndGetCourse(courseId)).thenReturn(course);
        when(homeworkTaskRepo.findById(taskId)).thenReturn(Optional.of(homeworkTask));

        assertDoesNotThrow(() -> authorHomeworkTaskService.deleteTask(courseId, lessonId, taskId));

        verify(courseUtils, times(1)).validateAndGetCourse(courseId);
        verify(homeworkTaskRepo, times(1)).findById(taskId);
        verify(homeworkTaskRepo, times(1)).delete(homeworkTask);
    }

    private ResponseHomeworkTaskDto createResponseHomeworkTaskDto() {
        ResponseHomeworkTaskDto responseHomeworkTaskDto = new ResponseHomeworkTaskDto();
        responseHomeworkTaskDto.setId(1L);
        responseHomeworkTaskDto.setDescription("Descr");
        responseHomeworkTaskDto.setTitle("New HW");
        responseHomeworkTaskDto.setDeadlineDate(OffsetDateTime.now(ZoneOffset.UTC));
        responseHomeworkTaskDto.setCreateDate(OffsetDateTime.now());
        responseHomeworkTaskDto.setUpdateDate(OffsetDateTime.now());
        return responseHomeworkTaskDto;
    }
}
