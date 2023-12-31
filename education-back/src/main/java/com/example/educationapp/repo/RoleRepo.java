package com.example.educationapp.repo;

import com.example.educationapp.entity.ERole;
import com.example.educationapp.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo extends JpaRepository<Role, Long> {
    Role findByRoleName(ERole roleName);
}
