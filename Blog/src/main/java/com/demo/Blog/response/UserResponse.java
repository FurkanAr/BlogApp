package com.demo.Blog.response;

import com.demo.Blog.model.enums.UserRole;

public class UserResponse {
    private Long id;
    private String userName;
    private String fullName;
    private String email;
    private UserRole role;


    public UserResponse() {
    }

    public UserResponse(Long id, String userName, String fullName, String email, UserRole role) {
        this.id = id;
        this.userName = userName;
        this.fullName = fullName;
        this.email = email;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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


    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }


}
