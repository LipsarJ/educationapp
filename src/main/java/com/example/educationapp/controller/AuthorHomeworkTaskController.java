package com.example.educationapp.controller;

import com.example.educationapp.dto.request.RequestHomeworkTaskDto;
import com.example.educationapp.dto.response.ResponseHomeworkTaskDto;
import com.example.educationapp.service.AuthorHomeworkTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/author/homework-tasks")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('AUTHOR')")
public class AuthorHomeworkTaskController {

    private final AuthorHomeworkTaskService authorHomeworkTaskService;

    @GetMapping("/{courseId}/{lessonId}")
    public ResponseEntity<List<ResponseHomeworkTaskDto>> getAllTasks(@PathVariable Long courseId, @PathVariable Long lessonId) {
        List<ResponseHomeworkTaskDto> tasks = authorHomeworkTaskService.getAllTasks(courseId, lessonId);
        return ResponseEntity.ok(tasks);
    }

    @PostMapping("/{courseId}/{lessonId}")
    public ResponseEntity<ResponseHomeworkTaskDto> createTask(@PathVariable Long courseId, @PathVariable Long lessonId,
                                                        @RequestBody RequestHomeworkTaskDto requestHomeworkTaskDto) {
        return ResponseEntity.ok(authorHomeworkTaskService.createTask(courseId, lessonId, requestHomeworkTaskDto));
    }

    @GetMapping("/{courseId}/{lessonId}/{id}")
    public ResponseEntity<ResponseHomeworkTaskDto> getTask(@PathVariable Long courseId, @PathVariable Long lessonId,
                                                   @PathVariable Long id) {
        ResponseHomeworkTaskDto responseHomeworkTaskDto = authorHomeworkTaskService.getTask(courseId, lessonId, id);
        return ResponseEntity.ok(responseHomeworkTaskDto);
    }

    @PutMapping("/{courseId}/{lessonId}/{id}")
    public ResponseEntity<ResponseHomeworkTaskDto> updateTask(@PathVariable Long courseId, @PathVariable Long lessonId,
                                                      @PathVariable Long id, @RequestBody RequestHomeworkTaskDto requestHomeworkTaskDto) {
        ResponseHomeworkTaskDto updatedHomeworkTask = authorHomeworkTaskService.updateTask(courseId, lessonId, id, requestHomeworkTaskDto);
        return ResponseEntity.ok(updatedHomeworkTask);
    }

    @DeleteMapping("/{courseId}/{lessonId}/{id}")
    public void deleteTask(@PathVariable Long courseId, @PathVariable Long lessonId,
                                          @PathVariable Long id) {
        authorHomeworkTaskService.deleteTask(courseId, lessonId, id);
    }
}
