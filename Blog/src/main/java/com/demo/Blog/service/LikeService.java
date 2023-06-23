package com.demo.Blog.service;

import com.demo.Blog.converter.LikeConverter;
import com.demo.Blog.exception.like.LikeNotFoundException;
import com.demo.Blog.exception.like.UserAlreadyLikedException;
import com.demo.Blog.exception.membership.MembershipIsExpiredException;
import com.demo.Blog.exception.messages.Messages;
import com.demo.Blog.model.Like;
import com.demo.Blog.model.Membership;
import com.demo.Blog.model.Post;
import com.demo.Blog.model.User;
import com.demo.Blog.repository.LikeRepository;
import com.demo.Blog.request.LikeRequest;
import com.demo.Blog.response.LikeResponse;
import com.demo.Blog.utils.MembershipUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final LikeConverter likeConverter;
    private final UserService userService;
    private final PostService postService;
    private final MembershipService membershipService;

    public LikeService(LikeRepository likeRepository, LikeConverter likeConverter, UserService userService, PostService postService, MembershipService membershipService) {
        this.likeRepository = likeRepository;
        this.likeConverter = likeConverter;
        this.userService = userService;
        this.postService = postService;
        this.membershipService = membershipService;
    }


    public List<LikeResponse> getAllLikesWithParam(Optional<Long> userId, Optional<Long> postId) {

        if(userId.isPresent() && postId.isPresent()){
            return likeConverter.convert(likeRepository.findByUserIdAndPostId(userId.get(), postId.get()));
        } else if (userId.isPresent()) {
            return likeConverter.convert(likeRepository.findByUserId(userId.get()));
        } else if (postId.isPresent()){
            return likeConverter.convert(likeRepository.findByPostId(postId.get()));
        }
        return likeConverter.convert(likeRepository.findAll());

    }

    public LikeResponse createLike(LikeRequest likeRequest) {
        User user = userService.findUserById(likeRequest.getUserId());
        Post post = postService.getPostById(likeRequest.getPostId());
        post.getLikeList().forEach(like -> {
            if(like.getUser().equals(user)){
              throw new UserAlreadyLikedException(Messages.Like.LIKE_EXIST + post.getId());
            }
        });
        Membership membership = membershipService.getMembershipByUserId(likeRequest.getUserId());

        if (!MembershipUtil.isMembershipActive(membership)) {
            membershipService.deleteMembershipById(membership.getId());
            throw new MembershipIsExpiredException(Messages.Membership.EXPIRED);
        }

        Like like = likeRepository.save(likeConverter.convert(post,user));
        return likeConverter.convert(like);
    }

    public LikeResponse getOneLike(Long likeId) {
        return likeConverter.convert(getLikeById(likeId));
    }

    public String deleteLikeById(Long likeId) {
        Like like = getLikeById(likeId);
        likeRepository.delete(like);
        return like.getId().toString();
    }

    public Like getLikeById(Long likeId){
        return likeRepository.findById(likeId).orElseThrow(() ->
                new LikeNotFoundException(Messages.Like.NOT_EXISTS_BY_ID + likeId));
    }

    public void deleteByUserId(Long userId) {
        userService.findUserById(userId);
        List<Like> likes = likeRepository.findAllByUserId(userId);
        likeRepository.deleteAll(likes);
    }
}
