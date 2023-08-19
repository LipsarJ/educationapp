package com.example.educationapp.controller;

import com.example.educationapp.dto.CourseDto;
import com.example.educationapp.service.AuthorCourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/author/courses")
@RequiredArgsConstructor
public class AuthorCourseController {
    private final AuthorCourseService authorCourseService;
    @GetMapping
    public ResponseEntity<List<CourseDto>> getAllCoursesForAuthor(){
        List<CourseDto> courses = authorCourseService.getAllCoursesForAuthor();
        return ResponseEntity.ok(courses);
    }

    @PostMapping
    public ResponseEntity<CourseDto> createCourse(@RequestBody CourseDto courseDto){
        authorCourseService.createCourse(courseDto);
        return ResponseEntity.ok(courseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDto> getCourse(@PathVariable Long id) {
        CourseDto courseDto = authorCourseService.getCourse(id);
        return ResponseEntity.ok(courseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseDto> updateCourse(@PathVariable Long id, @RequestBody CourseDto courseDto) {
        CourseDto updatedCourseDto = authorCourseService.updateCourse(id, courseDto);
        return ResponseEntity.ok(updatedCourseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        authorCourseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }
}
