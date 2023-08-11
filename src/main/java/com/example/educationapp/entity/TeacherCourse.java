package com.example.educationapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "teacher_courses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeacherCourse {

    @EmbeddedId
    private TeacherCourseId id;

    @ManyToOne
    @MapsId("teacherId")
    private User teacher;

    @ManyToOne
    @MapsId("courseId")
    private Course course;
}
