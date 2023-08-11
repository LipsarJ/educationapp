package com.example.educationapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "author_courses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorCourse {

    @EmbeddedId
    private AuthorCourseId id;

    @ManyToOne
    @MapsId("authorId")
    private User author;

    @ManyToOne
    @MapsId("courseId")
    private Course course;
}
