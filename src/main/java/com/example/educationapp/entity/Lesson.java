package com.example.educationapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "lessons")
@NoArgsConstructor
@AllArgsConstructor
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String lessonName;

    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LessonStatus status;

    @Column(nullable = false)
    private Timestamp createDate;

    @Column(nullable = false)
    private Timestamp updateDate;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course lessonsCourse;

    @OneToMany(mappedBy = "mediaLesson")
    private List<MediaLesson> mediaLessonSet = new ArrayList<MediaLesson>();

    @OneToMany(mappedBy = "lesson")
    private List<HomeworkTask> homeworkTaskList = new ArrayList<HomeworkTask>();
}
