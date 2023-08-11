package com.example.educationapp.entity;

import java.io.Serializable;

public class TeacherCourseId implements Serializable {

    private Integer teacherId;
    private Integer courseId;

    public TeacherCourseId() {
    }

    public TeacherCourseId(Integer teacherId, Integer courseId) {
        this.teacherId = teacherId;
        this.courseId = courseId;
    }

    // Override equals and hashCode methods
    // ...
}
