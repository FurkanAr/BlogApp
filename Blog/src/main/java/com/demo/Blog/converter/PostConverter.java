package com.demo.Blog.converter;

import com.demo.Blog.model.Post;
import com.demo.Blog.model.Tag;
import com.demo.Blog.model.User;
import com.demo.Blog.request.PostRequest;
import com.demo.Blog.request.PostUpdateRequest;
import com.demo.Blog.response.PostResponse;
import com.demo.Blog.response.PostUpdateResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Component
public class PostConverter {

    private final CommentConverter commentConverter;
    private final LikeConverter likeConverter;
    private final TagConverter tagConverter;

    public PostConverter(CommentConverter commentConverter, LikeConverter likeConverter, TagConverter tagConverter) {
        this.commentConverter = commentConverter;
        this.likeConverter = likeConverter;
        this.tagConverter = tagConverter;
    }


    public PostResponse convert(Post post) {
        PostResponse postResponse = new PostResponse();
        postResponse.setId(post.getId());
        postResponse.setText(post.getText());
        postResponse.setTitle(post.getTitle());
        postResponse.setPicture(post.getPicture());
        postResponse.setPublicationDate(post.getPublicationDate());
        if (post.getUpdateDate() != null) {
            postResponse.setUpdateDate(post.getUpdateDate());
        }
        postResponse.setUserId(post.getUser().getId());
        postResponse.setCommentResponseList(commentConverter.convert(post.getCommentList()));
        postResponse.setLikeResponseList(likeConverter.convert(post.getLikeList()));
        postResponse.setTagResponseList(tagConverter.convert(new ArrayList<>(post.getTags())));
        return postResponse;
    }

    public Post convert(PostRequest postRequest, User user, List<Tag> tags) {
        Post post = new Post();
        post.setTitle(postRequest.getTitle());
        post.setText(postRequest.getText());
        post.setPicture(postRequest.getPicture());
        post.setUser(user);
        post.setPublicationDate(LocalDate.now());
        post.setTags(new HashSet<>(tags));
        return post;
    }

    public List<PostResponse> convert(List<Post> postList) {
        List<PostResponse> postResponses = new ArrayList<>();
        postList.forEach(post -> postResponses.add(convert(post)));
        return postResponses;
    }

    public PostUpdateResponse update(Post post) {
        PostUpdateResponse postResponse = new PostUpdateResponse();
        postResponse.setId(post.getId());
        postResponse.setText(post.getText());
        postResponse.setTitle(post.getTitle());
        postResponse.setPicture(post.getPicture());
        postResponse.setPublicationDate(post.getPublicationDate());
        postResponse.setUpdateDate(post.getUpdateDate());
        postResponse.setUserId(post.getUser().getId());
        postResponse.setCommentResponseList(commentConverter.convert(post.getCommentList()));
        postResponse.setLikeResponseList(likeConverter.convert(post.getLikeList()));
        postResponse.setTagResponseList(tagConverter.convert(new ArrayList<>(post.getTags())));
        return postResponse;
    }

    public Post update(Post post, PostUpdateRequest postUpdateRequest) {
        post.setText(postUpdateRequest.getText());
        post.setTitle(postUpdateRequest.getTitle());
        post.setPicture(postUpdateRequest.getPicture());
        post.setUpdateDate(LocalDate.now());
        return post;
    }
}
