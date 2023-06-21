package com.demo.Blog.converter;

import com.demo.Blog.model.Comment;
import com.demo.Blog.model.Post;
import com.demo.Blog.model.User;
import com.demo.Blog.request.CommentRequest;
import com.demo.Blog.response.CommentResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class CommentConverter {

    public CommentResponse convert(Comment comment){
        CommentResponse commentResponse = new CommentResponse();
        commentResponse.setId(comment.getId());
        commentResponse.setCreateDate(comment.getCreateDate());
        commentResponse.setText(comment.getText());
        commentResponse.setPostId(comment.getPost().getId());
        commentResponse.setUserId(comment.getUser().getId());
        return  commentResponse;
    }

    public Comment convert(CommentRequest commentRequest, Post post, User user){
        Comment comment = new Comment();
        comment.setCreateDate(LocalDateTime.now());
        comment.setPost(post);
        comment.setText(commentRequest.getText());
        comment.setUser(user);
        return comment;
    }


    public List<CommentResponse> convert(List<Comment> commentList){
        List<CommentResponse> commentResponses = new ArrayList<>();
        commentList.stream().forEach(comment -> commentResponses.add(convert(comment)));
        return commentResponses;
    }

}
