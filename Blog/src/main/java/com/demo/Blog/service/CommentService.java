package com.demo.Blog.service;

import com.demo.Blog.converter.CommentConverter;
import com.demo.Blog.exception.comment.CommentNotFoundException;
import com.demo.Blog.exception.membership.MembershipIsExpiredException;
import com.demo.Blog.exception.messages.Messages;
import com.demo.Blog.model.Comment;
import com.demo.Blog.model.Membership;
import com.demo.Blog.model.Post;
import com.demo.Blog.model.User;
import com.demo.Blog.repository.CommentRepository;
import com.demo.Blog.request.CommentRequest;
import com.demo.Blog.request.CommentUpdateRequest;
import com.demo.Blog.response.CommentResponse;
import com.demo.Blog.utils.MembershipUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentConverter commentConverter;
    private final UserService userService;
    private final PostService postService;
    private final MembershipService membershipService;

    public CommentService(CommentRepository commentRepository, CommentConverter commentConverter, UserService userService, PostService postService, MembershipService membershipService) {
        this.commentRepository = commentRepository;
        this.commentConverter = commentConverter;
        this.userService = userService;
        this.postService = postService;
        this.membershipService = membershipService;
    }

    public List<CommentResponse> getAllCommentsWithParam(Optional<Long> userId, Optional<Long> postId) {

        if(userId.isPresent() && postId.isPresent()){
            return commentConverter.convert(commentRepository.findByUserIdAndPostId(userId.get(), postId.get()));
        } else if (userId.isPresent()) {
            return commentConverter.convert(commentRepository.findByUserId(userId.get()));
        } else if (postId.isPresent()){
            return commentConverter.convert(commentRepository.findByPostId(postId.get()));
        }
        return commentConverter.convert(commentRepository.findAll());

    }

    public CommentResponse getOneComment(Long commentId) {
        return commentConverter.convert(getCommentById(commentId));
    }

    public Comment getCommentById(Long commentId){
        return commentRepository.findById(commentId).orElseThrow(() ->
                new CommentNotFoundException(Messages.Comment.NOT_EXISTS_BY_ID + commentId));
    }

    public CommentResponse createComment(CommentRequest newComment) {
        Membership membership = membershipService.getMembershipByUserId(newComment.getUserId());

        if (!MembershipUtil.isMembershipActive(membership)) {
            membershipService.deleteMembershipById(membership.getId());
            throw new MembershipIsExpiredException(Messages.Membership.EXPIRED);
        }

        User user = userService.findUserById(newComment.getUserId());
        Post post = postService.getPostById(newComment.getPostId());
        Comment comment = commentRepository.save(commentConverter.convert(newComment, post, user));
        return commentConverter.convert(comment);
    }

    public CommentResponse updateComment(Long commentId, CommentUpdateRequest commentUpdateRequest) {
        Comment comment = getCommentById(commentId);
        comment.setText(commentUpdateRequest.getText());
        comment.setUpdateDate(LocalDateTime.now());
        return commentConverter.convert(commentRepository.save(comment));
    }

    public String deleteCommentById(Long commentId) {
        Comment comment = getCommentById(commentId);
        commentRepository.delete(comment);
        return comment.getId().toString();
    }

    public void deleteByUserId(Long userId) {
        userService.findUserById(userId);
        List<Comment> comments = commentRepository.findByUserId(userId);
        commentRepository.deleteAll(comments);
    }
}
