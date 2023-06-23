package com.demo.Blog.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public class UserEmailUpdateRequest {

    @NotEmpty(message = "Please enter your using email")
    @Email(message = "Invalid Email. Please enter proper Email")
    private String oldEmail;
    @NotEmpty(message = "Please enter your new email")
    @Email(message = "Invalid Email. Please enter proper Email")
    private String newEmail;
    public UserEmailUpdateRequest() {
    }

    public UserEmailUpdateRequest(String oldEmail, String newEmail) {
        this.oldEmail = oldEmail;
        this.newEmail = newEmail;
    }

    public String getOldEmail() {
        return oldEmail;
    }

    public void setOldEmail(String oldEmail) {
        this.oldEmail = oldEmail;
    }

    public String getNewEmail() {
        return newEmail;
    }

    public void setNewEmail(String newEmail) {
        this.newEmail = newEmail;
    }
}
