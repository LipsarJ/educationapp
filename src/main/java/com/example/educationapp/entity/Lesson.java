package com.example.educationapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.security.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "lessons")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Override
    public int hashCode() {
        return 612;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj == null) return false;
        if(getClass() != obj.getClass()) return false;
        Lesson other = (Lesson) obj;
        return id != null && id.equals(other.getId());
    }

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
