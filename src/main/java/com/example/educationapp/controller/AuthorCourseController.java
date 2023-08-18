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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        List<CourseDto> courses = authorCourseService.getAllCoursesForAuthor(username);
        return ResponseEntity.ok(courses);
    }

    @PostMapping
    public ResponseEntity<CourseDto> createCourse(@RequestBody CourseDto courseDto){
        authorCourseService.createCourse(courseDto);
        return ResponseEntity.ok(courseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDto> getCourse(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        CourseDto courseDto = authorCourseService.getCourse(id, username);
        return ResponseEntity.ok(courseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseDto> updateCourse(@PathVariable Long id, @RequestBody CourseDto courseDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        CourseDto updatedCourseDto = authorCourseService.updateCourse(id, courseDto, username);
        return ResponseEntity.ok(updatedCourseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        authorCourseService.deleteCourse(id, username);
        return ResponseEntity.noContent().build();
    }
}
