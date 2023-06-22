package com.demo.Blog.request;

public class UserPasswordUpdateRequest {

    private String userName;
    private String newPassword;
    private String oldPassword;

    public UserPasswordUpdateRequest() {
    }

    public UserPasswordUpdateRequest(String userName, String newPassword, String oldPassword) {
        this.userName = userName;
        this.newPassword = newPassword;
        this.oldPassword = oldPassword;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }
}
