package com.example.educationapp.entity;

import java.io.Serializable;

public class AuthorCourseId implements Serializable {

    private Integer authorId;
    private Integer courseId;

    public AuthorCourseId() {
    }

    public AuthorCourseId(Integer authorId, Integer courseId) {
        this.authorId = authorId;
        this.courseId = courseId;
    }

    // Override equals and hashCode methods
    // ...
}
