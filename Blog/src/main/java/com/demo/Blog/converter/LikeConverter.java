package com.demo.Blog.converter;

import com.demo.Blog.model.Like;
import com.demo.Blog.model.Post;
import com.demo.Blog.model.User;
import com.demo.Blog.response.LikeResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class LikeConverter {

    Logger logger = LoggerFactory.getLogger(getClass());

    public LikeResponse convert(Like like){
        logger.info("convert to Response method started");
        LikeResponse likeResponse = new LikeResponse();
        likeResponse.setId(like.getId());
        likeResponse.setPostId(like.getPost().getId());
        likeResponse.setUserId(like.getUser().getId());
        logger.info("convert to Response method successfully worked");
        return likeResponse;
    }

    public Like convert(Post post, User user){
        logger.info("convert to Like method started");
        Like like = new Like();
        like.setPost(post);
        like.setUser(user);
        logger.info("convert to Like method successfully worked");
        return like;
    }

    public List<LikeResponse> convert(List<Like> likeList){
        logger.info("convert likeList to likeResponses method started");
        List<LikeResponse> likeResponses = new ArrayList<>();
        likeList.forEach(like -> likeResponses.add(convert(like)));
        logger.info("convert likeList to likeResponses method successfully worked");
        return likeResponses;
    }

}
