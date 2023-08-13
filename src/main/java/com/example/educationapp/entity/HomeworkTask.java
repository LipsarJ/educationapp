package com.example.educationapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "homework_task")
@NoArgsConstructor
@AllArgsConstructor
public class HomeworkTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    private Timestamp deadlineDate;

    @Column(nullable = false)
    private Timestamp createDate;

    @Column(nullable = false)
    private Timestamp updateDate;

    @ManyToOne
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;

    @OneToMany(mappedBy = "task")
    private List<MediaHomeworkTask> homeworkTaskList = new ArrayList<MediaHomeworkTask>();

    @OneToMany(mappedBy = "taskDone")
    private List<HomeworkDone> homeworkDoneList = new ArrayList<HomeworkDone>();
}
