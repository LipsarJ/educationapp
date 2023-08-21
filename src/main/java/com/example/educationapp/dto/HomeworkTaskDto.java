package com.example.educationapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HomeworkTaskDto {
    private Long id;
    private String title;
    private String description;
    private OffsetDateTime deadlineDate;
    private OffsetDateTime createDate;
    private OffsetDateTime updateDate;
}
