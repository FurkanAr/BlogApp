package com.demo.Blog.request;

import com.demo.Blog.model.enums.UserRole;
import com.demo.Blog.validator.UserRoleSubset;

import javax.validation.constraints.*;


public class UserRequest {
    @NotEmpty(message = "Please enter your username")
    @Size(max = 15, min = 5, message = "Username: Invalid username, Username size should be between 5 to 15")
    private String userName;
    @NotEmpty(message = "Please enter your fullName")
    private String fullName;
    @NotEmpty(message = "Please enter your Email")
    @Email(message = "Invalid Email. Please enter proper Email")
    private String email;
    @NotEmpty(message = "Please enter your password")
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,20}$", message = "Password: " +
            "At least one upper case English letter," +
            "At least one lower case English letter," +
            "At least one digit," +
            "At least one special character," +
            "Min 8 characters," +
            "Max 20 characters")
    private String password;
    @NotEmpty(message = "Please enter your role.")
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
