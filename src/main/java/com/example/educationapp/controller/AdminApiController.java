package com.example.educationapp.controller;

import com.example.educationapp.controlleradvice.ErrorResponse;
import com.example.educationapp.dto.request.UpdateUserDto;
import com.example.educationapp.dto.response.admin.UserAdminResponseDto;
import com.example.educationapp.service.AdminApiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/user")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminApiController {

    private final AdminApiService adminApiService;

    @PutMapping("/{id}")
    @Operation(
            summary = "Обновить роли пользователя",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Возвращает пользователя с обновленными данными",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = UserAdminResponseDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Если запрос некорректный",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    public UserAdminResponseDto updateUser(@RequestBody UpdateUserDto updateUserDto, @PathVariable Long id) {
        return adminApiService.updateUser(updateUserDto, id);
    }
}
