package com.demo.Blog.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

public class UserPasswordUpdateRequest {

    @NotEmpty(message = "Please enter your username")
    private String userName;

    @NotEmpty(message = "Please enter your new password")
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,20}$", message = "Password: " +
            "At least one upper case English letter," +
            "At least one lower case English letter," +
            "At least one digit," +
            "At least one special character," +
            "Min 8 characters," +
            "Max 20 characters")
    private String newPassword;
    @NotEmpty(message = "Please enter your using password")
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
