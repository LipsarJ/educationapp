package com.example.educationapp.controller;

import com.example.educationapp.dto.HomeworkTaskDto;
import com.example.educationapp.service.AuthorHomeworkTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/author/homework-tasks")
@RequiredArgsConstructor
public class AuthorHomeworkTaskController {

    private final AuthorHomeworkTaskService authorHomeworkTaskService;

    @GetMapping("/{courseId}/{lessonId}")
    public ResponseEntity<List<HomeworkTaskDto>> getAllTasks(@PathVariable Long courseId, @PathVariable Long lessonId) {
        List<HomeworkTaskDto> tasks = authorHomeworkTaskService.getAllTasks(courseId, lessonId);
        return ResponseEntity.ok(tasks);
    }

    @PostMapping("/{courseId}/{lessonId}")
    public ResponseEntity<HomeworkTaskDto> createTask(@PathVariable Long courseId, @PathVariable Long lessonId,
                                                      @RequestBody HomeworkTaskDto homeworkTaskDto) {
        authorHomeworkTaskService.createTask(courseId, lessonId, homeworkTaskDto);
        return ResponseEntity.ok(homeworkTaskDto);
    }

    @GetMapping("/{courseId}/{lessonId}/{id}")
    public ResponseEntity<HomeworkTaskDto> getTask(@PathVariable Long courseId, @PathVariable Long lessonId,
                                                   @PathVariable Long id) {
        HomeworkTaskDto homeworkTaskDto = authorHomeworkTaskService.getTask(courseId, lessonId, id);
        return ResponseEntity.ok(homeworkTaskDto);
    }

    @PutMapping("/{courseId}/{lessonId}/{id}")
    public ResponseEntity<HomeworkTaskDto> updateTask(@PathVariable Long courseId, @PathVariable Long lessonId,
                                                      @PathVariable Long id, @RequestBody HomeworkTaskDto homeworkTaskDto) {
        HomeworkTaskDto updatedHomeworkTask = authorHomeworkTaskService.updateTask(courseId, lessonId, id, homeworkTaskDto);
        return ResponseEntity.ok(updatedHomeworkTask);
    }

    @DeleteMapping("/{courseId}/{lessonId}/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long courseId, @PathVariable Long lessonId,
                                          @PathVariable Long id) {
        authorHomeworkTaskService.deleteTask(courseId, lessonId, id);
        return ResponseEntity.noContent().build();
    }
}
