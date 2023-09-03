package com.example.educationapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class EducationappApplication {

    public static void main(String[] args) {
        SpringApplication.run(EducationappApplication.class, args);
    }

}
