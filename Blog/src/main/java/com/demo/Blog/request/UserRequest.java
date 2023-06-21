package com.demo.Blog.request;

import com.demo.Blog.model.enums.UserRole;
import com.demo.Blog.validator.UserRoleSubset;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UserRequest {
    @NotBlank(message = "Please enter your username")
    @Size(max = 15, min = 5, message = "Invalid Name, Size should be between 5 to 15")
    private String userName;
    @NotBlank(message = "Please enter your fullName")
    private String fullName;
    @NotBlank(message = "Please enter your Email")
    @Email(message = "Invalid Email. Please enter proper Email")
    private String email;
    @NotBlank(message = "Please enter your password")
    @Size(max = 20, min = 8, message = "Invalid password, Size should be between 8 to 20")
    private String password;
    @NotBlank(message = "Please enter your role. ADMIN or USER")
    @UserRoleSubset(enumClass = UserRole.class)
    private String role;

    public UserRequest() {
    }

    public UserRequest(String userName, String fullName, String email, String password, String role) {
        this.userName = userName;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
