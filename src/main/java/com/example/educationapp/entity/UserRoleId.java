package com.example.educationapp.entity;

import java.io.Serializable;

public class UserRoleId implements Serializable {

    private Integer userId;
    private String roleName;

    public UserRoleId() {
    }

    public UserRoleId(Integer userId, String roleName) {
        this.userId = userId;
        this.roleName = roleName;
    }

    // Override equals and hashCode methods
    // ...
}
