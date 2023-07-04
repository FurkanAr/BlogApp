package com.demo.Blog.converter;

import com.demo.Blog.model.Post;
import com.demo.Blog.model.Tag;
import com.demo.Blog.model.User;
import com.demo.Blog.request.PostRequest;
import com.demo.Blog.request.PostUpdateRequest;
import com.demo.Blog.response.PostResponse;
import com.demo.Blog.response.PostUpdateResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    Logger logger = LoggerFactory.getLogger(getClass());

    public PostConverter(CommentConverter commentConverter, LikeConverter likeConverter, TagConverter tagConverter) {
        this.commentConverter = commentConverter;
        this.likeConverter = likeConverter;
        this.tagConverter = tagConverter;
    }

    public PostResponse convert(Post post) {
        logger.info("convert to Response method started");
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
        logger.info("convert to Response method successfully worked");
        return postResponse;
    }

    public Post convert(PostRequest postRequest, User user, List<Tag> tags) {
        logger.info("convert to Post method started");
        Post post = new Post();
        post.setTitle(postRequest.getTitle());
        post.setText(postRequest.getText());
        post.setPicture(postRequest.getPicture());
        post.setUser(user);
        post.setPublicationDate(LocalDate.now());
        post.setTags(new HashSet<>(tags));
        logger.info("convert to Post method successfully worked");
        return post;
    }

    public List<PostResponse> convert(List<Post> postList) {
        logger.info("convert postList to postResponses method started");
        List<PostResponse> postResponses = new ArrayList<>();
        postList.forEach(post -> postResponses.add(convert(post)));
        logger.info("convert postList to postResponses method successfully worked");
        return postResponses;
    }

    public PostUpdateResponse update(Post post) {
        logger.info("update to PostUpdateResponse method started");
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
        logger.info("update to PostUpdateResponse method successfully worked");
        return postResponse;
    }

    public Post update(Post post, PostUpdateRequest postUpdateRequest) {
        logger.info("update to Post method started");
        post.setText(postUpdateRequest.getText());
        post.setTitle(postUpdateRequest.getTitle());
        post.setPicture(postUpdateRequest.getPicture());
        post.setUpdateDate(LocalDate.now());
        logger.info("update to Post method successfully worked");
        return post;
    }
}
