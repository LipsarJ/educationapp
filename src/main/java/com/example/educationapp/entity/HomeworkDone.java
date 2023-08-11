package com.example.educationapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.Timestamp;

@Entity
@Table(name = "homework_done")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HomeworkDone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    private HomeworkTask task;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    private Timestamp submission_date;
    private Integer grade;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private HomeworkDoneStatus status;
}
