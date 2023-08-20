package com.example.educationapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
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
