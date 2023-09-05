package com.example.educationapp.controller.author.management;

import com.example.educationapp.dto.request.authormanagement.AddOrRemoveAuthorsDto;
import com.example.educationapp.dto.request.authormanagement.AddOrRemoveTeachersDto;
import com.example.educationapp.dto.response.ResponseUserDto;
import com.example.educationapp.service.authormanagement.AuthorManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/author/courses")
@PreAuthorize("hasAuthority('AUTHOR')")
public class AuthorManagementController {
    private final AuthorManagementService authorManagementService;

    @GetMapping("/{id}/authors")
    public List<ResponseUserDto> getAuthorsForCourse(@PathVariable Long id) {
        return authorManagementService.getAllAuthorsForCourse(id);
    }

    @PutMapping("/{id}/add-authors")
    public List<ResponseUserDto> addAuthorsForCourse(@PathVariable Long id, @RequestBody AddOrRemoveAuthorsDto addOrRemoveAuthorsDto) {
        return authorManagementService.addAuthorsForCourse(id, addOrRemoveAuthorsDto);
    }

    @PutMapping("/{id}/remove-authors")
    public List<ResponseUserDto> removeAuthorsForCourse(@PathVariable Long id, @RequestBody AddOrRemoveAuthorsDto addOrRemoveAuthorsDto) {
        return authorManagementService.removeAuthorsForCourse(id, addOrRemoveAuthorsDto);
    }

    @GetMapping("/{id}/teachers")
    public List<ResponseUserDto> getTeachersForCourse(@PathVariable Long id) {
        return authorManagementService.getAllTeachersForCourse(id);
    }

    @PutMapping("/{id}/add-teachers")
    public List<ResponseUserDto> addTeachersForCourse(@PathVariable Long id, @RequestBody AddOrRemoveTeachersDto addOrRemoveTeachersDto) {
        return authorManagementService.addTeachersForCourse(id, addOrRemoveTeachersDto);
    }

    @PutMapping("/{id}/remove-teachers")
    public List<ResponseUserDto> removeTeachersForCourse(@PathVariable Long id, @RequestBody AddOrRemoveTeachersDto addOrRemoveTeachersDto) {
        return authorManagementService.removeTeachersForCourse(id, addOrRemoveTeachersDto);
    }
}
