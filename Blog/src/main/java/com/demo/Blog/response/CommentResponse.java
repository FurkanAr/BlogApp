package com.demo.Blog.response;

import java.time.LocalDateTime;

public class CommentResponse {

    private Long id;
    private Long postId;
    private Long userId;
    private String text;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    public CommentResponse() {
    }

    public CommentResponse(Long id, Long postId, Long userId, String text, LocalDateTime createDate, LocalDateTime updateDate) {
        this.id = id;
        this.postId = postId;
        this.userId = userId;
        this.text = text;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }
}
