package com.example.educationapp.controller;

import com.example.educationapp.dto.request.RequestLessonDto;
import com.example.educationapp.dto.response.ResponseLessonDto;
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
    public ResponseEntity<List<ResponseLessonDto>> getAllLessons(@PathVariable Long courseId) {
        List<ResponseLessonDto> lessons = authorLessonService.getAllLessons(courseId);
        return ResponseEntity.ok(lessons);
    }

    @PostMapping("/{courseId}")
    public ResponseEntity<ResponseLessonDto> createLesson(@PathVariable Long courseId, @RequestBody RequestLessonDto requestLessonDto) {
        return ResponseEntity.ok(authorLessonService.createLesson(courseId, requestLessonDto));
    }

    @GetMapping("/{courseId}/{id}")
    public ResponseEntity<ResponseLessonDto> getLesson(@PathVariable Long courseId, @PathVariable Long id) {
        ResponseLessonDto responseLessonDto = authorLessonService.getLesson(courseId, id);
        return ResponseEntity.ok(responseLessonDto);
    }

    @PutMapping("/{courseId}/{id}")
    public ResponseEntity<ResponseLessonDto> updateLesson(@PathVariable Long courseId, @PathVariable Long id, @RequestBody RequestLessonDto requestLessonDto) {
        ResponseLessonDto updatedLessonDto = authorLessonService.updateLesson(courseId, id, requestLessonDto);
        return ResponseEntity.ok(updatedLessonDto);
    }

    @DeleteMapping("/{courseId}/{id}")
    public void deleteLesson(@PathVariable Long courseId, @PathVariable Long id) {
        authorLessonService.deleteLesson(courseId, id);
    }
}
