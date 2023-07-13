package com.demo.Blog.service;

import com.demo.Blog.converter.CommentConverter;
import com.demo.Blog.exception.comment.CommentNotFoundException;
import com.demo.Blog.exception.membership.MembershipIsExpiredException;
import com.demo.Blog.exception.messages.Messages;
import com.demo.Blog.model.Comment;
import com.demo.Blog.model.Membership;
import com.demo.Blog.model.Post;
import com.demo.Blog.repository.CommentRepository;
import com.demo.Blog.request.CommentRequest;
import com.demo.Blog.request.CommentUpdateRequest;
import com.demo.Blog.response.CommentResponse;
import com.demo.Blog.utils.MembershipUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentConverter commentConverter;
    private final PostService postService;
    private final MembershipService membershipService;

    Logger logger = LoggerFactory.getLogger(getClass());

    public CommentService(CommentRepository commentRepository, CommentConverter commentConverter, PostService postService, MembershipService membershipService) {
        this.commentRepository = commentRepository;
        this.commentConverter = commentConverter;
        this.postService = postService;
        this.membershipService = membershipService;
    }

    public List<CommentResponse> getAllCommentsWithParam(Optional<Long> userId, Optional<Long> postId) {
        logger.info("getAllCommentsWithParam method started");

        if (userId.isPresent() && postId.isPresent()) {
            logger.info("Found comments by userId: {}, postId: {} ", userId.get(), postId.get());
            return commentConverter.convert(commentRepository.findByUserIdAndPostId(userId.get(), postId.get()));
        } else if (userId.isPresent()) {
            logger.info("Found comments by userId: {} ", userId.get());
            return commentConverter.convert(commentRepository.findByUserId(userId.get()));
        } else if (postId.isPresent()) {
            logger.info("Found comments by postId: {} ", postId.get());
            return commentConverter.convert(commentRepository.findByPostId(postId.get()));
        }
        logger.info("getAllCommentsWithParam method successfully worked");
        return commentConverter.convert(commentRepository.findAll());
    }

    public CommentResponse getOneComment(Long commentId) {
        logger.info("getOneComment method started");
        logger.info("getOneComment method successfully worked");
        return commentConverter.convert(getCommentById(commentId));
    }

    @Transactional
    public CommentResponse createComment(CommentRequest newComment) {
        logger.info("createComment method started");
        Membership membership = membershipService.getMembershipByUserId(newComment.getUserId());

        if (MembershipUtil.isMembershipActive(membership)) {
            logger.warn("User membership is expired, user: {} ", membership.getUser().getId());
            membershipService.deleteMembershipById(membership.getId());
            logger.info("User: {} membership deleted, membershipId: {} ", membership.getUser().getId(), membership.getId());
            throw new MembershipIsExpiredException(Messages.Membership.EXPIRED);
        }

        Post post = postService.getPostById(newComment.getPostId());

        Comment comment = commentRepository.save(commentConverter.convert(newComment, post, membership.getUser()));
        logger.info("Comment created: {} ", comment.getId());

        logger.info("createComment method successfully worked");
        return commentConverter.convert(comment);
    }

    @Transactional
    public CommentResponse updateComment(Long commentId, CommentUpdateRequest commentUpdateRequest) {
        logger.info("updateComment method started");
        Comment comment = getCommentById(commentId);
        comment.setText(commentUpdateRequest.getText());
        comment.setUpdateDate(LocalDateTime.now());
        logger.info("Comment updated: {} ", comment.getId());
        logger.info("updateComment method successfully worked");
        return commentConverter.convert(commentRepository.save(comment));
    }

    public String deleteCommentById(Long commentId) {
        logger.info("deleteCommentById method started");
        Comment comment = getCommentById(commentId);
        commentRepository.delete(comment);
        logger.info("Comment deleted: {} ", commentId);
        logger.info("deleteCommentById method successfully worked");
        return comment.getId().toString();
    }

    public Comment getCommentById(Long commentId) {
        logger.info("getCommentById method started");
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new CommentNotFoundException(Messages.Comment.NOT_EXISTS_BY_ID + commentId));
        logger.info("Found comment by commentId: {} ", commentId);
        logger.info("getCommentById method successfully worked");
        return comment;

    }

}
