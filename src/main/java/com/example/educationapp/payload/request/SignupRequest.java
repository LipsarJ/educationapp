package com.example.educationapp.payload.request;

import java.sql.Timestamp;
import java.util.Set;

import jakarta.validation.constraints.*;

public class SignupRequest {
    @NotBlank
    @Size(min = 3, max = 20)
    private String username;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    private Set<String> roles;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;

    @NotBlank
    private String middlename;

    @NotBlank
    private String firstname;

    @NotBlank
    private String lastname;

    @NotNull
    private Timestamp createDate;

    @NotNull
    private Timestamp updateDate;

    @NotNull
    private String userStatus;

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public Set<String> getRoles() {
        return  roles;
    }

    public String getPassword() {
        return password;
    }

    public String getMiddlename() {
        return middlename;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public Timestamp getUpdateDate() {
        return updateDate;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

