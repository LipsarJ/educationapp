package com.example.educationapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "homework_done")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class HomeworkDone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Override
    public int hashCode() {
        return 451;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj == null) return false;
        if(getClass() != obj.getClass()) return false;
        HomeworkDone other = (HomeworkDone) obj;
        return id != null && id.equals(other.getId());
    }

    private Timestamp submissionDate;
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
