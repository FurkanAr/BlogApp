package com.demo.Blog.response;


import java.time.LocalDate;
import java.util.List;

public class PostResponse {

    private Long id;
    private String title;
    private String text;
    private LocalDate publicationDate;
    private String picture;
    private Long userId;
    private List<CommentResponse> commentResponseList;
    private List<LikeResponse> likeResponseList;

    private List<TagResponse> tagResponseList;

    public PostResponse() {
    }

    public PostResponse(Long id, String title, String text, LocalDate publicationDate, String picture, Long userId, List<CommentResponse> commentResponseList, List<LikeResponse> likeResponseList, List<TagResponse> tagResponseList) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.publicationDate = publicationDate;
        this.picture = picture;
        this.userId = userId;
        this.commentResponseList = commentResponseList;
        this.likeResponseList = likeResponseList;
        this.tagResponseList = tagResponseList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
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

    public List<CommentResponse> getCommentResponseList() {
        return commentResponseList;
    }

    public void setCommentResponseList(List<CommentResponse> commentResponseList) {
        this.commentResponseList = commentResponseList;
    }

    public List<LikeResponse> getLikeResponseList() {
        return likeResponseList;
    }

    public void setLikeResponseList(List<LikeResponse> likeResponseList) {
        this.likeResponseList = likeResponseList;
    }

    public List<TagResponse> getTagResponseList() {
        return tagResponseList;
    }

    public void setTagResponseList(List<TagResponse> tagResponseList) {
        this.tagResponseList = tagResponseList;
    }
}
