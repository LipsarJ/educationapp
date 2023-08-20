package com.example.educationapp.controller;

import com.example.educationapp.dto.CourseDto;
import com.example.educationapp.service.AuthorCourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/author/courses")
@RequiredArgsConstructor
public class AuthorCourseController {
    private final AuthorCourseService authorCourseService;
    @GetMapping
    public List<CourseDto> getAllCoursesForAuthor(){
        List<CourseDto> courses = authorCourseService.getAllCoursesForAuthor();
        return courses;
    }

    @PostMapping
    public CourseDto createCourse(@RequestBody CourseDto courseDto){
        authorCourseService.createCourse(courseDto);
        return courseDto;
    }

    @GetMapping("/{id}")
    public CourseDto getCourse(@PathVariable Long id) {
        CourseDto courseDto = authorCourseService.getCourse(id);
        return courseDto;
    }

    @PutMapping("/{id}")
    public CourseDto updateCourse(@PathVariable Long id, @RequestBody CourseDto courseDto) {
        CourseDto updatedCourseDto = authorCourseService.updateCourse(id, courseDto);
        return updatedCourseDto;
    }

    @DeleteMapping("/{id}")
    public void deleteCourse(@PathVariable Long id) {
        authorCourseService.deleteCourse(id);
    }
}
