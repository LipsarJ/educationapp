package com.example.educationapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Set;

import static java.time.ZoneOffset.UTC;

@Entity
@Table(name = "users")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"studentCourseSet", "authorCourseSet", "teacherCourseSet"})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Override
    public int hashCode() {
        return 813;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj == null) return false;
        if(getClass() != obj.getClass()) return false;
        User other = (User) obj;
        return id != null && id.equals(other.getId());
    }

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String lastname;

    @Column
    private String middlename;

    @Column(nullable = false)
    private String firstname;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status;

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

    @ManyToMany
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roleSet = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "student_courses",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id"))
    private Set<Course> studentCourseSet = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "author_courses",
            joinColumns = @JoinColumn(name = "author_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id"))
    private Set<Course> authorCourseSet = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "teacher_courses",
            joinColumns = @JoinColumn(name = "teacher_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id"))
    private Set<Course> teacherCourseSet = new HashSet<>();

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", lastname='" + lastname + '\'' +
                ", middlename='" + middlename + '\'' +
                ", firstname='" + firstname + '\'' +
                ", password='" + password + '\'' +
                ", status=" + status +
                ", createDate=" + createDate +
                ", updateDate=" + updateDate +
                '}';
    }
}
