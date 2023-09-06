package com.example.educationapp.dto.request.management.teacher;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "ДТО, для получения id студентов, которых нужно добавить или удалить из курса.")
public class AddOrRemoveStudentsDto {
    List<Long> ids;
}
