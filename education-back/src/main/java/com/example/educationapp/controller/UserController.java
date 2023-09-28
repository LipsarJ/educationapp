package com.example.educationapp.controller;

import com.example.educationapp.controlleradvice.SimpleResponse;
import com.example.educationapp.dto.response.UserInfoDto;
import com.example.educationapp.dto.response.UserInfoPage;
import com.example.educationapp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

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
    public UserInfoPage getUsersWithPagination(
            @RequestParam(name = "filterText", required = false) @Schema(example = "Lipsar") String filterText,
            @Schema(name = "Параметры пагинации.", description = "Поле sort должно содержать название одного или нескольких полей " +
                    "сущности User, например lastname, firstname и т.д", implementation = Pageable.class) Pageable pageable) {
        Page<UserInfoDto> userPage = userService.getUsersWithPaginationAndFilter(filterText, pageable);

        return new UserInfoPage(
                userPage.getContent(),
                userPage.getTotalElements(),
                pageable.getPageNumber(),
                pageable.getPageSize()
        );
    }
}
