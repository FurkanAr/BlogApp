package com.demo.Blog.controller;

import com.demo.Blog.request.PostRequest;
import com.demo.Blog.request.PostUpdateRequest;
import com.demo.Blog.response.PostResponse;
import com.demo.Blog.response.PostUpdateResponse;
import com.demo.Blog.service.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    Logger logger = LoggerFactory.getLogger(getClass());

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts(@RequestParam Optional<Long> userId) {
        logger.debug("getAllPosts method started");
        List<PostResponse> postResponses = postService.getAllPosts(userId);
        logger.info("getAllPosts successfully worked");
        return ResponseEntity.ok(postResponses);
    }

    @PostMapping
    public ResponseEntity<PostResponse> createPost(@RequestBody @Valid PostRequest newPost) {
        logger.debug("createPost method started");
        PostResponse postResponse = postService.createPost(newPost);
        logger.info("createPost successfully worked, userId: {}", newPost.getUserId());
        return new ResponseEntity<>(postResponse, HttpStatus.CREATED);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponse> getOnePost(@PathVariable Long postId) {
        logger.debug("getOnePost method started");
        PostResponse postResponse = postService.getOnePostById(postId);
        logger.info("getOnePost successfully worked, postId: {}", postId);
        return ResponseEntity.ok(postResponse);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<PostUpdateResponse> updatePost(@PathVariable Long postId, @RequestBody @Valid PostUpdateRequest postUpdateRequest) {
        logger.debug("updatePost method started");
        PostUpdateResponse postResponse = postService.updatePost(postId, postUpdateRequest);
        logger.info("updatePost successfully worked, postId: {}", postId);
        return ResponseEntity.ok(postResponse);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deletePostById(@PathVariable Long postId) {
        logger.debug("deletePostById method started");
        String message = postService.deletePostById(postId);
        logger.info("deletePostById successfully worked, postId: {}", postId);
        return ResponseEntity.ok(message);
    }

    @GetMapping("/month")
    public ResponseEntity<List<PostResponse>> getAllThisMonthPosts() {
        logger.debug("getAllThisMonthPosts method started");
        List<PostResponse> postResponses = postService.getAllThisMonthPosts();
        logger.info("getAllThisMonthPosts successfully worked");
        return ResponseEntity.ok(postResponses);
    }

    @GetMapping("/category")
    public ResponseEntity<List<PostResponse>> getPostsByTag(@RequestParam("tag") String tag) {
        logger.debug("getPostsByTag method started");
        List<PostResponse> postResponses = postService.getPostByTag(tag);
        logger.info("getPostsByTag successfully worked, tag: {}", tag);
        return ResponseEntity.ok(postResponses);
    }
}
