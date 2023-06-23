package com.demo.Blog.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class UserUserNameUpdateRequest {
    @NotEmpty(message = "Please enter your username")
    private String oldUserName;
    @NotEmpty(message = "Please enter your new username")
    @Size(max = 15, min = 5, message = "Username: Invalid username, Username size should be between 5 to 15")
    private String newUserName;

    public UserUserNameUpdateRequest() {
    }

    public UserUserNameUpdateRequest(String oldUserName, String newUserName) {
        this.oldUserName = oldUserName;
        this.newUserName = newUserName;
    }

    public String getOldUserName() {
        return oldUserName;
    }

    public void setOldUserName(String oldUserName) {
        this.oldUserName = oldUserName;
    }

    public String getNewUserName() {
        return newUserName;
    }

    public void setNewUserName(String newUserName) {
        this.newUserName = newUserName;
    }
}
