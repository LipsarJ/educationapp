package com.example.educationapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "courses")
@NoArgsConstructor
@AllArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String courseName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CourseStatus status;

    @Column(nullable = false)
    private Timestamp createDate;

    @Column(nullable = false)
    private Timestamp updateDate;

    @ManyToMany(mappedBy = "studentCourseSet")
    private Set<User> students = new HashSet<>();

    @ManyToMany(mappedBy = "teacherCourseSet")
    private Set<User> teachers = new HashSet<>();

    @ManyToMany(mappedBy = "authorCourseSet")
    private Set<User> authors = new HashSet<>();

    @OneToMany(mappedBy = "lessonsCourse")
    private List<Lesson> lessonList = new ArrayList<Lesson>();
}
