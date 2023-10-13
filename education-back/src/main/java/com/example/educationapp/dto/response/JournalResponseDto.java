package com.example.educationapp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JournalResponseDto {
    private Long lessonId;
    private Long studentId;
    private Float percentage;
}
