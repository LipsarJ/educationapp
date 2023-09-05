package com.example.educationapp.controller.author.management;

import com.example.educationapp.dto.request.authormanagement.AddOrRemoveAuthorsDto;
import com.example.educationapp.dto.response.ResponseUserDto;
import com.example.educationapp.service.authormanagement.AuthorManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
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
}
