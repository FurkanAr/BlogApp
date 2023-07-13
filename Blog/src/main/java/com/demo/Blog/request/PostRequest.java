package com.demo.Blog.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

public class PostRequest {
    @NotEmpty(message = "Please enter your title")
    @Size(max = 50, min = 5, message = "Title: Invalid title, Title size should be between 5 to 50")
    private String title;

    @NotEmpty(message = "Please enter your text")
    @Size(max = 5000, min = 5, message = "Text: Invalid text, Text size should be between 5 to 5000")
    private String text;
    @NotEmpty(message = "Please enter your picture")
    private String picture;
    private Long userId;
    @NotEmpty(message = "Please enter your tag ids")
    private List<Long> tagIds;

    public PostRequest() {
    }

    public PostRequest(String title, String text, String picture, Long userId, List<Long> tagIds) {
        this.title = title;
        this.text = text;
        this.picture = picture;
        this.userId = userId;
        this.tagIds = tagIds;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<Long> getTagIds() {
        return tagIds;
    }

    public void setTagIds(List<Long> tagIds) {
        this.tagIds = tagIds;
    }

    @Override
    public String toString() {
        return "PostRequest{" +
                "title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", picture='" + picture + '\'' +
                ", userId=" + userId +
                ", tagIds=" + tagIds +
                '}';
    }
}
