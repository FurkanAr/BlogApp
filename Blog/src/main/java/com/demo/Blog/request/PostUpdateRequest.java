package com.demo.Blog.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class PostUpdateRequest {

    @NotEmpty(message = "Please enter your title")
    @Size(max = 50, min = 5, message = "Title: Invalid title, Title size should be between 5 to 50")
    private String title;
    @NotEmpty(message = "Please enter your text")
    @Size(max = 5000, min = 5, message = "Text: Invalid text, Text size should be between 5 to 5000")
    private String text;
    @NotEmpty(message = "Please enter your picture")
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
