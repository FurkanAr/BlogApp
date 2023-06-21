package com.demo.Blog.request;

public class CommentUpdateRequest {

    private String text;

    public CommentUpdateRequest() {
    }

    public CommentUpdateRequest(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
