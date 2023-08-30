package com.example.educationapp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserInfoPage {
    private List<UserInfoDto> userInfo;
    private long totalCount;
    private int page;
    private int countValuesPerPage;
}
