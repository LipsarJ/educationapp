package com.example.educationapp.controller.admin;

import com.example.educationapp.controlleradvice.SimpleResponse;
import com.example.educationapp.dto.request.UpdateUserDto;
import com.example.educationapp.dto.request.admin.UpdatePasswordDto;
import com.example.educationapp.dto.response.UserInfoDto;
import com.example.educationapp.dto.response.admin.UserAdminResponseDto;
import com.example.educationapp.dto.response.admin.UserInfoAdminPage;
import com.example.educationapp.service.admin.AdminApiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/user")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminApiController {

    private final AdminApiService adminApiService;

    @GetMapping()
    @Operation(summary = "Найти пользователей по ФИО или username с возможностью пагинации.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Возвращает информацию о найденных пользователях.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserInfoDto.class))),
            @ApiResponse(responseCode = "404", description = "Если пользователь не найден.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SimpleResponse.class))),
            @ApiResponse(responseCode = "400", description = "Если введены неверные параметры пагинации.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SimpleResponse.class)))
    })
    public UserInfoAdminPage getUsersWithPagination(
            @RequestParam(name = "filterText", required = false) @Schema(example = "Lipsar") String filterText,
            @Schema(name = "Параметры пагинации.", description = "Поле sort должно содержать название одного или нескольких полей " +
                    "сущности User, например lastname, firstname и т.д", implementation = Pageable.class) Pageable pageable) {
        Page<UserAdminResponseDto> userPage = adminApiService.getUsersWithPaginationAndFilter(filterText, pageable);

        return new UserInfoAdminPage(
                userPage.getContent(),
                userPage.getTotalElements(),
                pageable.getPageNumber(),
                pageable.getPageSize()
        );
    }

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
                                    schema = @Schema(implementation = SimpleResponse.class))
                    )
            }
    )
    public UserAdminResponseDto updateUser(@RequestBody UpdateUserDto updateUserDto, @PathVariable Long id) {
        return adminApiService.updateUser(updateUserDto, id);
    }

    @PutMapping("{id}/password")
    @Operation(
            summary = "Обновить пароль пользователя",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Возвращает строку с сообщением об успешном изменении пароля.",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = String.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Если пользователь не найден",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = SimpleResponse.class))
                    ),
            }
    )
    public UserAdminResponseDto updateUserPassword(@RequestBody UpdatePasswordDto updatePasswordDto, @PathVariable Long id) {
        return adminApiService.updateUserPassword(updatePasswordDto, id);
    }
}
