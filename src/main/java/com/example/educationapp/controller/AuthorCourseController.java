package com.example.educationapp.controller;

import com.example.educationapp.dto.request.RequestCourseDto;
import com.example.educationapp.dto.response.ResponseCourseDto;
import com.example.educationapp.service.AuthorCourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/author/courses")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('AUTHOR')")
public class AuthorCourseController {
    private final AuthorCourseService authorCourseService;

    @GetMapping
    public ResponseEntity<List<ResponseCourseDto>> getAllCoursesForAuthor(){
        List<ResponseCourseDto> courses = authorCourseService.getAllCoursesForAuthor();
        return ResponseEntity.ok(courses);
    }

    @PostMapping
    public ResponseEntity<ResponseCourseDto> createCourse(@RequestBody RequestCourseDto requestCourseDto){
        return ResponseEntity.ok(authorCourseService.createCourse(requestCourseDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseCourseDto> getCourse(@PathVariable Long id) {
        ResponseCourseDto responseCourseDto = authorCourseService.getCourse(id);
        return ResponseEntity.ok(responseCourseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseCourseDto> updateCourse(@PathVariable Long id, @RequestBody RequestCourseDto requestCourseDto) {
        ResponseCourseDto updatedCourseDto = authorCourseService.updateCourse(id, requestCourseDto);
        return ResponseEntity.ok(updatedCourseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        authorCourseService.deleteCourse(id);
        return ResponseEntity.ok().build();
    }
}
