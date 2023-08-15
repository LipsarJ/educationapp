package com.example.educationapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "courses")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Override
    public int hashCode() {
        return 871;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj == null) return false;
        if(getClass() != obj.getClass()) return false;
        Course other = (Course) obj;
        return id != null && id.equals(other.getId());
    }

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
    @JsonIgnore
    private Set<User> students = new HashSet<>();

    @ManyToMany(mappedBy = "teacherCourseSet")
    @JsonIgnore
    private Set<User> teachers = new HashSet<>();

    @ManyToMany(mappedBy = "authorCourseSet")
    @JsonIgnore
    private Set<User> authors = new HashSet<>();

    @OneToMany(mappedBy = "lessonsCourse")
    private List<Lesson> lessonList = new ArrayList<Lesson>();
}
