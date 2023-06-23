package com.demo.Blog.request;

import javax.validation.constraints.NotEmpty;

public class CommentRequest {

    private Long postId;
    private Long userId;
    @NotEmpty(message = "Please enter your comment")
    private String text;

    public CommentRequest() {
    }

    public CommentRequest(Long postId, Long userId, String text) {
        this.postId = postId;
        this.userId = userId;
        this.text = text;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
