package com.demo.Blog.request;

import javax.validation.constraints.NotEmpty;

public class LikeRequest {

    private Long postId;
    private Long userId;

    public LikeRequest() {
    }

    public LikeRequest(Long postId, Long userId) {
        this.postId = postId;
        this.userId = userId;
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
}
