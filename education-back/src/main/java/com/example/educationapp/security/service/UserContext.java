package com.example.educationapp.security.service;

import com.example.educationapp.dto.response.ResponseUserDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope
@Getter
@Setter(AccessLevel.PACKAGE)
public class UserContext {
    private ResponseUserDto userDto;
}
