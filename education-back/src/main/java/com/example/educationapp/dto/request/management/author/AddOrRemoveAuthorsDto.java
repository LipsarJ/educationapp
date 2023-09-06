package com.example.educationapp.dto.request.management.author;

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
@Schema(description = "ДТО для id пользователей, которых нужно удалить или добавить в список авторов курса.")
public class AddOrRemoveAuthorsDto {
    List<Long> ids;
}
