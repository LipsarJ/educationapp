package com.example.educationapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HomeworkTaskDto {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime deadlineDate;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
}
