package com.example.educationapp.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "media_homework_done")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MediaHomeworkDone {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long size;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MediaType mediaType;

    @ManyToOne
    @JoinColumn(name = "homework_done_id", nullable = false)
    private HomeworkDone homeworkDone;

    @Override
    public int hashCode() {
        return 913;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        MediaHomeworkDone other = (MediaHomeworkDone) obj;
        return id != null && id.equals(other.getId());
    }
}
