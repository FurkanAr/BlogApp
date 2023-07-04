package com.demo.Blog.controller;

import com.demo.Blog.request.CommentRequest;
import com.demo.Blog.request.CommentUpdateRequest;
import com.demo.Blog.response.CommentResponse;
import com.demo.Blog.service.CommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;
    Logger logger = LoggerFactory.getLogger(getClass());

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    public ResponseEntity<List<CommentResponse>> getAllComments(@RequestParam Optional<Long> userId,
                                                                @RequestParam Optional<Long> postId) {
        logger.info("getAllComments method started");
        List<CommentResponse> commentResponses = commentService.getAllCommentsWithParam(userId, postId);
        logger.info("getAllComments successfully worked");
        return ResponseEntity.ok(commentResponses);
    }

    @PostMapping
    public ResponseEntity<CommentResponse> createComment(@RequestBody @Valid CommentRequest newComment) {
        logger.info("createComment method started");
        CommentResponse commentResponse = commentService.createComment(newComment);
        logger.info("createComment successfully worked, postId: {} userId: {}",
                newComment.getPostId(), newComment.getUserId());
        return new ResponseEntity<>(commentResponse, HttpStatus.CREATED);
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentResponse> getOneComment(@PathVariable Long commentId) {
        logger.info("getOneComment method started");
        CommentResponse commentResponse = commentService.getOneComment(commentId);
        logger.info("getOneComment successfully worked, commentId: {}", commentId);
        return ResponseEntity.ok(commentResponse);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponse> updateComment(@PathVariable Long commentId,
                                                         @RequestBody @Valid CommentUpdateRequest commentUpdateRequest) {
        logger.info("updateComment method started");
        CommentResponse commentResponse = commentService.updateComment(commentId, commentUpdateRequest);
        logger.info("updateComment successfully worked, commentId: {}", commentId);
        return ResponseEntity.ok(commentResponse);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId) {
        logger.info("deleteComment method started");
        String message = commentService.deleteCommentById(commentId);
        logger.info("deleteComment successfully worked, commentId: {}", commentId);
        return ResponseEntity.ok(message);
    }

}
