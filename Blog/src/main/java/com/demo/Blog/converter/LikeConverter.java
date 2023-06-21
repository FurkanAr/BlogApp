package com.demo.Blog.converter;

import com.demo.Blog.model.Like;
import com.demo.Blog.model.Post;
import com.demo.Blog.model.User;
import com.demo.Blog.response.LikeResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class LikeConverter {

    public LikeResponse convert(Like like){
        LikeResponse likeResponse = new LikeResponse();
        likeResponse.setId(like.getId());
        likeResponse.setPostId(like.getPost().getId());
        likeResponse.setUserId(like.getUser().getId());
        return likeResponse;
    }

    public Like convert(Post post, User user){
        Like like = new Like();
        like.setPost(post);
        like.setUser(user);
        return like;
    }


    public List<LikeResponse> convert(List<Like> likeList){
        List<LikeResponse> likeResponses = new ArrayList<>();
        likeList.stream().forEach(like -> likeResponses.add(convert(like)));
        return likeResponses;
    }



}
