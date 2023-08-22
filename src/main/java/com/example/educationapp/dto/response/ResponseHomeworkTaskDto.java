package com.example.educationapp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseHomeworkTaskDto {
    private String title;
    private String description;
    private OffsetDateTime deadlineDate;
}
