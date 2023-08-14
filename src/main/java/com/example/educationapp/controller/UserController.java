package com.example.educationapp.controller;

import com.example.educationapp.dto.RoleDto;
import com.example.educationapp.dto.UserDto;
import com.example.educationapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDto>> findAll() {
        List<UserDto> userDtos = userService.findAll();
        return ResponseEntity.ok(userDtos);
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserDto> findByUsername(@PathVariable String username) {
        UserDto userDto = userService.findByUsername(username);
        return ResponseEntity.ok(userDto);
    }

    @PostMapping
    public ResponseEntity<UserDto> save(@RequestBody UserDto userDto) {
        UserDto savedUserDto = userService.save(userDto);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentRequest().path("/{username}")
                .buildAndExpand(savedUserDto.getUsername()).toUriString());
        return ResponseEntity.created(uri).body(savedUserDto);
    }

    @PostMapping("/{username}/addRoleToUser")
    public ResponseEntity<UserDto> addRoleToUser(@PathVariable String username, @RequestBody RoleDto roleDto) {
        UserDto updatedUserDto = userService.addRoleToUser(username, roleDto);
        return ResponseEntity.ok(updatedUserDto);
    }
}