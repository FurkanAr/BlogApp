package com.demo.Blog.controller;

import com.demo.Blog.request.CommentRequest;
import com.demo.Blog.request.CommentUpdateRequest;
import com.demo.Blog.response.CommentResponse;
import com.demo.Blog.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    public ResponseEntity<List<CommentResponse>> getAllComments(@RequestParam Optional<Long> userId,
                                                                @RequestParam Optional<Long> postId) {
        List<CommentResponse> commentResponses = commentService.getAllCommentsWithParam(userId, postId);
        return ResponseEntity.ok(commentResponses);
    }

    @PostMapping
    public ResponseEntity<CommentResponse> createComment(@RequestBody CommentRequest newComment) {
        CommentResponse commentResponse = commentService.createComment(newComment);
        return new ResponseEntity<>(commentResponse, HttpStatus.CREATED);
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentResponse> getOneComment(@PathVariable Long commentId) {
        CommentResponse commentResponse = commentService.getOneComment(commentId);
        return ResponseEntity.ok(commentResponse);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponse> updateComment(@PathVariable Long commentId,
                                                         @RequestBody CommentUpdateRequest commentUpdateRequest) {
        CommentResponse commentResponse = commentService.updateComment(commentId, commentUpdateRequest);
        return ResponseEntity.ok(commentResponse);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId) {
        String message = commentService.deleteCommentById(commentId);
        return ResponseEntity.ok(message);
    }

}
