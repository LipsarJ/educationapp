package com.example.educationapp.controller;

import com.example.educationapp.dto.LessonDto;
import com.example.educationapp.service.AuthorLessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/author/lessons")
public class AuthorLessonController {
    private final AuthorLessonService authorLessonService;

    @GetMapping("{courseId}")
    public ResponseEntity<List<LessonDto>> getAllLessons(@PathVariable Long courseId) {
        List<LessonDto> lessons = authorLessonService.getAllLessons(courseId);
        return ResponseEntity.ok(lessons);
    }

    @PostMapping("/{courseId}")
    public ResponseEntity<LessonDto> createLesson(@PathVariable Long courseId, @RequestBody LessonDto lessonDto) {
        authorLessonService.createLesson(courseId, lessonDto);
        return ResponseEntity.ok(lessonDto);
    }

    @GetMapping("/{courseId}/{id}")
    public ResponseEntity<LessonDto> getLesson(@PathVariable Long courseId, @PathVariable Long id) {
        LessonDto lessonDto = authorLessonService.getLesson(courseId, id);
        return ResponseEntity.ok(lessonDto);
    }

    @PutMapping("/{courseId}/{id}")
    public ResponseEntity<LessonDto> updateLesson(@PathVariable Long courseId, @PathVariable Long id, @RequestBody LessonDto lessonDto) {
        LessonDto updatedLessonDto = authorLessonService.updateLesson(courseId, id, lessonDto);
        return ResponseEntity.ok(updatedLessonDto);
    }

    @DeleteMapping("/{courseId}/{id}")
    public ResponseEntity<Void> deleteLesson(@PathVariable Long courseId, @PathVariable Long id) {
        authorLessonService.deleteLesson(courseId, id);
        return ResponseEntity.noContent().build();
    }
}
