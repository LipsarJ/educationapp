package com.example.educationapp.payload.request;

import java.sql.Timestamp;
import java.util.Set;

import com.example.educationapp.entity.UserStatus;
import jakarta.validation.constraints.*;

public class SignupRequest {
    @NotBlank
    @Size(min = 3, max = 20)
    private String username;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    private Set<String> role;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;

    @NotBlank
    private String middlename;

    @NotBlank
    private String firstname;

    @NotBlank
    private String lastname;

    @NotBlank
    private Timestamp createDate;

    @NotBlank
    private Timestamp updateDate;

    @NotBlank
    private UserStatus userStatus;

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public Set<String> getRole() {
        return role;
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

    public UserStatus getUserStatus() {
        return userStatus;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(Set<String> role) {
        this.role = role;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public void setUpdateDate(Timestamp updateDate) {
        this.updateDate = updateDate;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }
}

