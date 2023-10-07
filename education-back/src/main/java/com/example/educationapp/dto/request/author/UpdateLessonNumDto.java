package com.example.educationapp.dto.request.author;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateLessonNumDto {

    @Schema(description = "Id урока которому нужно обновить номер", example = "1")
    private Long id;
    @Schema(description = "Новый номер урока", example = "2")
    private Integer num;
}
