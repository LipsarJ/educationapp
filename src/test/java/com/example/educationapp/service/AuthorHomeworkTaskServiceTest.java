package com.example.educationapp.service;

import com.example.educationapp.dto.request.RequestHomeworkTaskDto;
import com.example.educationapp.dto.response.ResponseHomeworkTaskDto;
import com.example.educationapp.entity.HomeworkTask;
import com.example.educationapp.entity.Lesson;
import com.example.educationapp.entity.MediaHomeworkTask;
import com.example.educationapp.mapper.HomeworkTaskMapper;
import com.example.educationapp.repo.HomeworkTaskRepo;
import com.example.educationapp.repo.LessonRepo;
import com.example.educationapp.repo.MediaHomeworkTaskRepo;
import com.example.educationapp.utils.CourseUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
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

    @Mock
    private MediaHomeworkTaskRepo mediaHomeworkTaskRepo;

    @InjectMocks
    private AuthorHomeworkTaskService homeworkTaskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void testGetAllTasks() {
        Long courseId = 1L;
        Long lessonId = 2L;
        Lesson lesson = new Lesson();
        lesson.setId(lessonId);
        when(lessonRepo.findById(lessonId)).thenReturn(Optional.of(lesson));

        List<HomeworkTask> tasks = new ArrayList<>();
        tasks.add(new HomeworkTask());
        tasks.add(new HomeworkTask());
        when(homeworkTaskRepo.findAllByLesson(lesson)).thenReturn(tasks);

        when(homeworkTaskMapper.toResponseDto(any(HomeworkTask.class)))
                .thenReturn(new ResponseHomeworkTaskDto());

        // Execute
        List<ResponseHomeworkTaskDto> result = homeworkTaskService.getAllTasks(courseId, lessonId);

        // Assert
        assertNotNull(result);
        assertEquals(tasks.size(), result.size());
        verify(courseUtils).validateAndGetCourse(courseId);
        verify(lessonRepo).findById(lessonId);
        verify(homeworkTaskRepo).findAllByLesson(lesson);
        verify(homeworkTaskMapper, times(tasks.size())).toResponseDto(any(HomeworkTask.class));
    }

    @Test
    void testCreateTask() {
        // Prepare
        Long courseId = 1L;
        Long lessonId = 2L;
        RequestHomeworkTaskDto requestDto = new RequestHomeworkTaskDto();
        Lesson lesson = new Lesson();
        lesson.setId(lessonId);

        when(lessonRepo.findById(lessonId)).thenReturn(Optional.of(lesson));
        when(homeworkTaskRepo.existsByTitle(requestDto.getTitle())).thenReturn(false);
        when(homeworkTaskMapper.toEntity(requestDto)).thenReturn(new HomeworkTask());
        when(homeworkTaskRepo.save(any(HomeworkTask.class))).thenReturn(new HomeworkTask());
        when(homeworkTaskMapper.toResponseDto(any(HomeworkTask.class)))
                .thenReturn(new ResponseHomeworkTaskDto());

        // Execute
        ResponseHomeworkTaskDto result = homeworkTaskService.createTask(courseId, lessonId, requestDto);

        // Assert
        assertNotNull(result);
        verify(courseUtils).validateAndGetCourse(courseId);
        verify(lessonRepo).findById(lessonId);
        verify(homeworkTaskRepo).existsByTitle(requestDto.getTitle());
        verify(homeworkTaskMapper).toEntity(requestDto);
        verify(homeworkTaskRepo).save(any(HomeworkTask.class));
        verify(lessonRepo).save(lesson);
        verify(homeworkTaskMapper).toResponseDto(any(HomeworkTask.class));
    }

    @Test
    void testGetTask() {
        Long courseId = 1L;
        Long lessonId = 2L;
        Long taskId = 3L;
        HomeworkTask task = new HomeworkTask();
        task.setId(taskId);

        when(courseUtils.validateAndGetCourse(courseId)).thenReturn(null);
        when(lessonRepo.findById(lessonId)).thenReturn(Optional.of(new Lesson()));
        when(homeworkTaskRepo.findById(taskId)).thenReturn(Optional.of(task));
        when(homeworkTaskMapper.toResponseDto(any(HomeworkTask.class)))
                .thenReturn(new ResponseHomeworkTaskDto());

        ResponseHomeworkTaskDto result = homeworkTaskService.getTask(courseId, lessonId, taskId);

        assertNotNull(result);
        verify(courseUtils).validateAndGetCourse(courseId);
        verify(lessonRepo).findById(lessonId);
        verify(homeworkTaskRepo).findById(taskId);
        verify(homeworkTaskMapper).toResponseDto(any(HomeworkTask.class));
    }

    @Test
    void testUpdateTask() {
        Long courseId = 1L;
        Long lessonId = 2L;
        Long taskId = 3L;

        RequestHomeworkTaskDto requestDto = new RequestHomeworkTaskDto();
        requestDto.setTitle("New Task Title");
        requestDto.setDescription("New Task Description");
        requestDto.setDeadlineDate(OffsetDateTime.now());

        HomeworkTask existingTask = new HomeworkTask();
        existingTask.setId(taskId);
        existingTask.setTitle("Existing Task Title");

        when(courseUtils.validateAndGetCourse(courseId)).thenReturn(null);
        when(lessonRepo.findById(lessonId)).thenReturn(Optional.of(new Lesson()));
        when(homeworkTaskRepo.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(homeworkTaskRepo.existsByTitleAndIdNot(requestDto.getTitle(), taskId)).thenReturn(false);
        when(homeworkTaskMapper.toEntity(requestDto)).thenReturn(existingTask);
        when(homeworkTaskRepo.save(existingTask)).thenReturn(existingTask);

        ResponseHomeworkTaskDto responseDto = new ResponseHomeworkTaskDto();
        responseDto.setId(taskId);
        responseDto.setTitle(requestDto.getTitle());
        responseDto.setDescription(requestDto.getDescription());
        responseDto.setDeadlineDate(requestDto.getDeadlineDate());

        when(homeworkTaskMapper.toResponseDto(existingTask)).thenReturn(responseDto);

        ResponseHomeworkTaskDto updatedTask = homeworkTaskService.updateTask(courseId, lessonId, taskId, requestDto);

        assertNotNull(updatedTask);
        assertEquals(responseDto.getId(), updatedTask.getId());
        assertEquals(responseDto.getTitle(), updatedTask.getTitle());
        assertEquals(responseDto.getDescription(), updatedTask.getDescription());
        assertEquals(responseDto.getDeadlineDate(), updatedTask.getDeadlineDate());

        verify(courseUtils).validateAndGetCourse(courseId);
        verify(lessonRepo).findById(lessonId);
        verify(homeworkTaskRepo).findById(taskId);
        verify(homeworkTaskRepo).existsByTitleAndIdNot(requestDto.getTitle(), taskId);
        verify(homeworkTaskMapper).toEntity(requestDto);
        verify(homeworkTaskRepo).save(existingTask);
        verify(homeworkTaskMapper).toResponseDto(existingTask);
    }
    @Test
    void testDeleteTask() {
        // Prepare
        Long courseId = 1L;
        Long lessonId = 2L;
        Long taskId = 3L;
        Lesson lesson = new Lesson();
        lesson.setId(lessonId);
        HomeworkTask task = new HomeworkTask();
        task.setId(taskId);
        MediaHomeworkTask mediaHomeworkTask = new MediaHomeworkTask();

        when(courseUtils.validateAndGetCourse(courseId)).thenReturn(null);
        when(lessonRepo.findById(lessonId)).thenReturn(Optional.of(lesson));
        when(homeworkTaskRepo.findById(taskId)).thenReturn(Optional.of(task));
        when(mediaHomeworkTaskRepo.save(any(MediaHomeworkTask.class))).thenReturn(mediaHomeworkTask);

        // Execute
        homeworkTaskService.deleteTask(courseId, lessonId, taskId);

        // Assert
        verify(courseUtils).validateAndGetCourse(courseId);
        verify(lessonRepo).findById(lessonId);
        verify(homeworkTaskRepo).findById(taskId);
        verify(mediaHomeworkTaskRepo, times(0)).save(any(MediaHomeworkTask.class));
        verify(lessonRepo).save(lesson);
        verify(homeworkTaskRepo).delete(task);
    }

}