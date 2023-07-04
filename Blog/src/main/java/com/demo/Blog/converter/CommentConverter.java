package com.demo.Blog.converter;

import com.demo.Blog.model.Comment;
import com.demo.Blog.model.Post;
import com.demo.Blog.model.User;
import com.demo.Blog.request.CommentRequest;
import com.demo.Blog.response.CommentResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class CommentConverter {

    Logger logger = LoggerFactory.getLogger(getClass());

    public CommentResponse convert(Comment comment){
        logger.info("convert to Response method started");
        CommentResponse commentResponse = new CommentResponse();
        commentResponse.setId(comment.getId());
        commentResponse.setCreateDate(comment.getCreateDate());
        if(comment.getUpdateDate() != null){
            commentResponse.setUpdateDate(comment.getUpdateDate());
        }
        commentResponse.setText(comment.getText());
        commentResponse.setPostId(comment.getPost().getId());
        commentResponse.setUserId(comment.getUser().getId());
        logger.info("convert to Response method successfully worked");
        return  commentResponse;
    }

    public Comment convert(CommentRequest commentRequest, Post post, User user){
        logger.info("convert to Comment method started");
        Comment comment = new Comment();
        comment.setCreateDate(LocalDateTime.now());
        comment.setPost(post);
        comment.setText(commentRequest.getText());
        comment.setUser(user);
        logger.info("convert to Comment method successfully worked");
        return comment;
    }


    public List<CommentResponse> convert(List<Comment> commentList){
        logger.info("convert commentList to commentResponses method started");
        List<CommentResponse> commentResponses = new ArrayList<>();
        commentList.forEach(comment -> commentResponses.add(convert(comment)));
        logger.info("convert commentList to commentResponses method successfully worked");
        return commentResponses;
    }

}
