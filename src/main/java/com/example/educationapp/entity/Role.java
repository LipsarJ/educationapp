package com.example.educationapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "roles")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    @Column(name = "role_name")
    private String roleName;

    @Override
    public int hashCode() {
        return Objects.hashCode(roleName);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Role other = (Role) obj;
        return Objects.equals(roleName, other.getRoleName());
    }

    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new HashSet<>();
}
