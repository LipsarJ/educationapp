package com.example.educationapp.dto.response.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Schema(description = "Страница с информацией о пользователе")
public class UserInfoAdminPage {
    private List<UserAdminResponseDto> userInfo;
    private long totalCount;
    private int page;
    private int countValuesPerPage;
}
