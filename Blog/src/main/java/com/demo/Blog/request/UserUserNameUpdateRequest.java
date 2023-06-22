package com.demo.Blog.request;

public class UserUserNameUpdateRequest {

    private String oldUserName;
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
