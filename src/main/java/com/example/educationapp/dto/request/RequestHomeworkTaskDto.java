package com.example.educationapp.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestHomeworkTaskDto {
    private Long id;
    private String title;
    private String description;
    private OffsetDateTime updateDate;
}
