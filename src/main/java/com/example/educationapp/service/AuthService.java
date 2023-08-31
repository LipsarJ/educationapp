package com.example.educationapp.service;

import com.example.educationapp.controlleradvice.ErrorResponse;
import com.example.educationapp.dto.request.LoginDto;
import com.example.educationapp.dto.request.SignupDto;
import com.example.educationapp.dto.response.UserInfoDto;
import com.example.educationapp.entity.RefreshToken;
import com.example.educationapp.entity.User;
import com.example.educationapp.entity.UserStatus;
import com.example.educationapp.exception.TokenRefreshException;
import com.example.educationapp.repo.UserRepo;
import com.example.educationapp.security.jwt.JwtUtils;
import com.example.educationapp.security.service.RefreshTokenService;
import com.example.educationapp.security.service.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepo userRepo;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public ResponseEntity<?> authenticateUser(LoginDto loginDto) {
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

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                    .header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString())
                    .body(new UserInfoDto(userDetails.getId(),
                            userDetails.getUsername(),
                            userDetails.getEmail(),
                            roles));
        } catch (AuthenticationException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("Invalid username or password"));
        }
    }

    @Transactional
    public ResponseEntity<?> registerUser(SignupDto signUpDto) {
        if (userRepo.existsByUsername(signUpDto.username())) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Error: Username is already taken!"));
        }

        if (userRepo.existsByEmail(signUpDto.email())) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Error: Email is already in use!"));
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

        return ResponseEntity.ok(new ErrorResponse("User registered successfully!"));
    }

    @Transactional
    public ResponseEntity<?> logoutUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!"anonymousUser".equals(principal.toString())) {
            Long userId = ((UserDetailsImpl) principal).getId();
            refreshTokenService.deleteByUserId(userId);
        }

        ResponseCookie jwtCookie = jwtUtils.getCleanJwtCookie();
        ResponseCookie jwtRefreshCookie = jwtUtils.getCleanJwtRefreshCookie();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString())
                .body(new ErrorResponse("You've been signed out!"));
    }

    @Transactional
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        String refreshToken = jwtUtils.getJwtRefreshFromCookies(request);

        if (refreshToken != null && refreshToken.length() > 0) {
            return refreshTokenService.findByToken(refreshToken)
                    .map(refreshTokenService::verifyExpiration)
                    .map(RefreshToken::getUser)
                    .map(user -> {
                        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(user);

                        return ResponseEntity.ok()
                                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                                .body(new ErrorResponse("Token is refreshed successfully!"));
                    })
                    .orElseThrow(() -> new TokenRefreshException(refreshToken, "Refresh token is not in the database!"));
        }

        return ResponseEntity.badRequest().body(new ErrorResponse("Refresh Token is empty!"));
    }
}

