package com.example.educationapp.service;

import com.example.educationapp.entity.Role;
import com.example.educationapp.entity.User;
import com.example.educationapp.repo.RoleRepo;
import com.example.educationapp.repo.UserRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {
    private static final String USER_NOT_FOUND_MESSAGE = "User with username %s not found";
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;

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

    public User save(User user) {
        log.info("Saving user {} to the database", user.getUsername());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    public User addRoleToUser(String username, String roleName) {
        log.info("Adding role {} to user {}", roleName, username);
        User user = userRepo.findByUsername(username);
        Role role = roleRepo.findById(roleName).orElse(null); // Используйте orElse(null), чтобы избежать NPE
        if (role != null) {
            user.getRoleSet().add(role);
        } else {
            log.warn("Role {} not found. Role not added to user {}", roleName, username);
        }
        return user;
    }
}
