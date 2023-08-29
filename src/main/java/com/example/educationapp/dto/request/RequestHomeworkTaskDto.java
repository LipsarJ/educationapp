package com.example.educationapp.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestHomeworkTaskDto {
    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @NotBlank
    private OffsetDateTime deadlineDate;
}
