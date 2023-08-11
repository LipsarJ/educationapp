package com.example.educationapp.entity;

import java.io.Serializable;

public class StudentCourseId implements Serializable {

    private Integer studentId;
    private Integer courseId;

    public StudentCourseId() {
    }

    public StudentCourseId(Integer studentId, Integer courseId) {
        this.studentId = studentId;
        this.courseId = courseId;
    }

    // Override equals and hashCode methods
    // ...
}
