package com.demo.Blog.request;

public class UserEmailUpdateRequest {

    private String oldEmail;
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
