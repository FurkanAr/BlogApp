package com.demo.Blog.request;

public class PostUpdateRequest {

    private String title;
    private String text;
    private String picture;

    public PostUpdateRequest() {
    }

    public PostUpdateRequest(String title, String text, String picture) {
        this.title = title;
        this.text = text;
        this.picture = picture;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
