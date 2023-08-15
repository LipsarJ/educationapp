package com.example.educationapp.repo;

import com.example.educationapp.entity.ERole;
import com.example.educationapp.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface RoleRepo extends JpaRepository<Role, String> {
    Optional<Role> findByRoleName(ERole name);
}
