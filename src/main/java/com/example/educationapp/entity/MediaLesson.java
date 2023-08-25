package com.example.educationapp.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "media_lesson")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MediaLesson {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @Override
    public int hashCode() {
        return 313;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj == null) return false;
        if(getClass() != obj.getClass()) return false;
        MediaLesson other = (MediaLesson) obj;
        return id != null && id.equals(other.getId());
    }

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long size;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MediaType mediaType;

    @ManyToOne
    @JoinColumn(name = "lesson_id")
    private Lesson mediaLesson;
}
