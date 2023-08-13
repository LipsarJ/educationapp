package com.example.educationapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "homework_done")
@NoArgsConstructor
@AllArgsConstructor
public class HomeworkDone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Timestamp submission_date;
    private Integer grade;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private HomeworkDoneStatus status;

    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    private HomeworkTask task;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @OneToMany(mappedBy = "homeworkDone")
    private List<MediaHomeworkDone> mediaHomeworkDoneList = new ArrayList<MediaHomeworkDone>();
}
