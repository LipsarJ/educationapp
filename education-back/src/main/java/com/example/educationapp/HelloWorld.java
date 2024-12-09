package com.example.educationapp;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/helloworld")
public class HelloWorld {
    @GetMapping
    public String helloworld() {
        return "HelloWorld!";
    }
}
