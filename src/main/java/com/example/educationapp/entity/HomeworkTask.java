package com.example.educationapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static java.time.ZoneOffset.UTC;

@Entity
@Table(name = "homework_task")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class HomeworkTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Override
    public int hashCode() {
        return 738;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        HomeworkTask other = (HomeworkTask) obj;
        return id != null && id.equals(other.getId());
    }

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column
    private LocalDateTime deadlineDate;

    @Column(nullable = false)
    private LocalDateTime createDate;

    @Column(nullable = false)
    private LocalDateTime updateDate;

    @PrePersist
    void onCreate() {
        updateDate = LocalDateTime.now(ZoneId.from(UTC));
        createDate = LocalDateTime.now(ZoneId.from(UTC));
    }

    @PreUpdate
    void onUpdate() {
        updateDate = LocalDateTime.now(ZoneId.from(UTC));
    }

    @ManyToOne
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;

    @OneToMany(mappedBy = "taskMedia")
    private List<MediaHomeworkTask> homeworkTaskMediaList = new ArrayList<MediaHomeworkTask>();

    @OneToMany(mappedBy = "task")
    private List<HomeworkDone> homeworkDoneList = new ArrayList<HomeworkDone>();
}
