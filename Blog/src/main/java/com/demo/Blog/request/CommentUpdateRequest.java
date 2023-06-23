package com.demo.Blog.request;

import javax.validation.constraints.NotEmpty;

public class CommentUpdateRequest {

    @NotEmpty(message = "Please enter your comment")
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
