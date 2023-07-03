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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final LikeConverter likeConverter;
    private final UserService userService;
    private final PostService postService;
    private final MembershipService membershipService;

    Logger logger = LoggerFactory.getLogger(getClass());

    public LikeService(LikeRepository likeRepository, LikeConverter likeConverter, UserService userService, PostService postService, MembershipService membershipService) {
        this.likeRepository = likeRepository;
        this.likeConverter = likeConverter;
        this.userService = userService;
        this.postService = postService;
        this.membershipService = membershipService;
    }

    public List<LikeResponse> getAllLikesWithParam(Optional<Long> userId, Optional<Long> postId) {
        logger.debug("getAllLikesWithParam method started");

        if (userId.isPresent() && postId.isPresent()) {
            logger.info("Found likes by userId: {}, postId: {} ", userId.get(), postId.get());
            return likeConverter.convert(likeRepository.findByUserIdAndPostId(userId.get(), postId.get()));
        } else if (userId.isPresent()) {
            logger.info("Found likes by userId: {} ", userId.get());
            return likeConverter.convert(likeRepository.findByUserId(userId.get()));
        } else if (postId.isPresent()) {
            logger.info("Found likes by postId: {} ", postId.get());
            return likeConverter.convert(likeRepository.findByPostId(postId.get()));
        }
        logger.info("getAllLikesWithParam method successfully worked");
        return likeConverter.convert(likeRepository.findAll());

    }
    @Transactional
    public LikeResponse createLike(LikeRequest likeRequest) {
        logger.debug("createLike method started");
        User user = userService.findUserById(likeRequest.getUserId());
        Post post = postService.getPostById(likeRequest.getPostId());
        Membership membership = membershipService.getMembershipByUserId(likeRequest.getUserId());

        if (!MembershipUtil.isMembershipActive(membership)) {
            logger.warn("User membership is expired, user: {} ", membership.getUser().getId());
            membershipService.deleteMembershipById(membership.getId());
            logger.info("User: {} membership deleted, membershipId: {} ", membership.getUser().getId(), membership.getId());
            throw new MembershipIsExpiredException(Messages.Membership.EXPIRED);
        }

        post.getLikeList().forEach(like -> {
            if (like.getUser().equals(user)) {
                logger.warn("User already liked, user: {} ", user.getId());
                throw new UserAlreadyLikedException(Messages.Like.LIKE_EXIST + post.getId());
            }
        });

        Like like = likeRepository.save(likeConverter.convert(post, user));
        logger.info("Like created: {} ", like.getId());

        logger.info("createLike method successfully worked");
        return likeConverter.convert(like);
    }

    public LikeResponse getOneLike(Long likeId) {
        logger.debug("getOneLike method started");
        logger.info("getOneLike method successfully worked");
        return likeConverter.convert(getLikeById(likeId));
    }

    public String deleteLikeById(Long likeId) {
        logger.debug("deleteLikeById method started");
        Like like = getLikeById(likeId);
        likeRepository.delete(like);
        logger.info("Like deleted: {} ", likeId);
        logger.info("deleteLikeById method successfully worked");
        return like.getId().toString();
    }

    public Like getLikeById(Long likeId) {
        logger.debug("getLikeById method started");

        Like like = likeRepository.findById(likeId).orElseThrow(() ->
                new LikeNotFoundException(Messages.Like.NOT_EXISTS_BY_ID + likeId));
        logger.info("Found like by likeId: {} ", likeId);

        logger.info("getLikeById method successfully worked");
        return like;
    }

    public void deleteByUserId(Long userId) {
        logger.debug("deleteByUserId method started");
        userService.findUserById(userId);
        List<Like> likes = likeRepository.findAllByUserId(userId);
        likeRepository.deleteAll(likes);
        logger.info("User: {}, all likes deleted ", userId);
        logger.info("deleteByUserId method successfully worked");
    }
}
