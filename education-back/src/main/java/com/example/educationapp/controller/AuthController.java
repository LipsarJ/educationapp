package com.example.educationapp.controller;

import com.example.educationapp.controlleradvice.ErrorResponse;
import com.example.educationapp.controlleradvice.LoginErrors;
import com.example.educationapp.dto.request.LoginDto;
import com.example.educationapp.dto.request.SignupDto;
import com.example.educationapp.dto.response.UserLoginDto;
import com.example.educationapp.entity.RefreshToken;
import com.example.educationapp.entity.User;
import com.example.educationapp.entity.UserStatus;
import com.example.educationapp.exception.TokenRefreshException;
import com.example.educationapp.repo.RefreshTokenRepo;
import com.example.educationapp.repo.UserRepo;
import com.example.educationapp.security.jwt.JwtUtils;
import com.example.educationapp.security.service.RefreshTokenService;
import com.example.educationapp.security.service.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserRepo userRepo;
    private final RefreshTokenRepo refreshTokenRepo;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/signin")
    @Operation(summary = "Аутентификация пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Возвращает информацию о пользователе с токенами",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UserLoginDto.class))),
            @ApiResponse(responseCode = "401", description = "Если неверные данные для аутентификации",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginDto loginDto) {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginDto.username(), loginDto.password()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

            List<String> roles = userDetails.getAuthorities().stream()
                    .map(item -> item.getAuthority())
                    .collect(Collectors.toList());

            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

            ResponseCookie jwtRefreshCookie = jwtUtils.generateRefreshJwtCookie(refreshToken.getToken());

            UserLoginDto userLoginDto = new UserLoginDto(userDetails.getId(),
                    userDetails.getUsername(),
                    userDetails.getEmail(),
                    roles);

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                    .header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString())
                    .body(userLoginDto);
        } catch (AuthenticationException ex) {
            return ResponseEntity.status(org.springframework.http.HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("Invalid username or password", LoginErrors.BAD_CREDITIANS));
        }
    }

    @PostMapping("/signup")
    @Operation(summary = "Регистрация пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Возвращает сообщение об успешной регистрации пользователя",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Если данные пользователя некорректны",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupDto signUpDto) {
        if (userRepo.existsByUsername(signUpDto.username())) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Error: Username is already taken!", LoginErrors.USERNAME_TAKEN));
        }

        if (userRepo.existsByEmailIgnoreCase(signUpDto.email())) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Error: Email is already in use!", LoginErrors.EMAIL_TAKEN));
        }

        User user = new User();
        user.setUsername(signUpDto.username());
        user.setEmail(signUpDto.email());
        user.setPassword(encoder.encode(signUpDto.password()));
        user.setMiddlename(signUpDto.middlename());
        user.setFirstname(signUpDto.firstname());
        user.setLastname(signUpDto.lastname());
        user.setStatus(UserStatus.ACTIVE);

        userRepo.save(user);

        return ResponseEntity.ok(new ErrorResponse("User registered successfully!", null));
    }

    @PostMapping("/signout")
    @Operation(summary = "Выход пользователя")
    @ApiResponse(responseCode = "200", description = "Возвращает сообщение о успешном выходе пользователя",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<?> logoutUser() {

        ResponseCookie jwtCookie = jwtUtils.getCleanJwtCookie();
        ResponseCookie jwtRefreshCookie = jwtUtils.getCleanJwtRefreshCookie();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString())
                .body(new ErrorResponse("You've been signed out!", null));
    }

    @PostMapping("/refreshtoken")
    @Operation(summary = "Обновление токена")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Возвращает сообщение об успешном обновлении токена",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Если токен обновления недействителен",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        String refreshToken = jwtUtils.getJwtRefreshFromCookies(request);

        if (refreshToken != null && refreshToken.length() > 0) {
            return refreshTokenRepo.findByToken(refreshToken)
                    .map(refreshTokenService::verifyExpiration)
                    .map(RefreshToken::getUser)
                    .map(user -> {
                        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(user);

                        return ResponseEntity.ok()
                                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                                .body(new ErrorResponse("Token is refreshed successfully!", null));
                    })
                    .orElseThrow(() -> new TokenRefreshException(refreshToken, "Refresh token is not in the database!"));
        }

        return ResponseEntity.badRequest().body(new ErrorResponse("Refresh Token is empty!", null));
    }
}
