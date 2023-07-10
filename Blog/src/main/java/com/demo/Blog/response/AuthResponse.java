package com.demo.Blog.response;

public class AuthResponse {

    private Long userId;
    private String message;
    private String userName;
    private String token;

    public AuthResponse() {
    }

    public AuthResponse(Long userId, String message, String userName, String token) {
        this.userId = userId;
        this.message = message;
        this.userName = userName;
        this.token = token;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "AuthResponse{" +
                "userId=" + userId +
                ", message='" + message + '\'' +
                ", userName='" + userName + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
