package com.demo.Blog.request;


public class MailRequest {

    private String userName;
    private String email;
    private String fullName;
    private String message;

    public MailRequest() {
    }

    public MailRequest(String userName, String email, String fullName, String message) {
        this.userName = userName;
        this.email = email;
        this.fullName = fullName;
        this.message = message;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "MailRequest{" +
                "userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", fullName='" + fullName + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
