package com.example.educationapp.service;

import com.example.educationapp.dto.RoleDto;
import com.example.educationapp.dto.UserDto;
import com.example.educationapp.entity.Role;
import com.example.educationapp.entity.User;
import com.example.educationapp.mapper.UserMapper;
import com.example.educationapp.repo.RoleRepo;
import com.example.educationapp.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {
    private static final String USER_NOT_FOUND_MESSAGE = "User with username %s not found";
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format(USER_NOT_FOUND_MESSAGE, username));
        }

        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        for (Role role : user.getRoleSet()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleName()));
        }

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities);
    }

    public UserDto save(UserDto userDto) {
        log.info("Saving user {} to the database", userDto.getUsername());
        User user = userMapper.userDtoToUser(userDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepo.save(user);
        return userMapper.userToUserDto(savedUser);
    }

    public UserDto addRoleToUser(String username, RoleDto roleDto) {
        log.info("Adding role {} to user {}", roleDto.getRoleName(), username);
        User user = userRepo.findByUsername(username);
        Role role = roleRepo.findById(roleDto.getRoleName()).orElse(null);
        if (role != null) {
            user.getRoleSet().add(role);
        } else {
            log.warn("Role {} not found. Role not added to user {}", roleDto.getRoleName(), username);
        }
        User updatedUser = userRepo.save(user);
        return userMapper.userToUserDto(updatedUser);
    }

    public UserDto findByUsername(String username) {
        User user = userRepo.findByUsername(username);
        return userMapper.userToUserDto(user);
    }

    public List<UserDto> findAll() {
        List<User> users = userRepo.findAll();
        return users.stream()
                .map(userMapper::userToUserDto)
                .collect(Collectors.toList());
    }
}