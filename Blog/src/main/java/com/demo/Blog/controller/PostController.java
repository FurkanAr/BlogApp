package com.demo.Blog.controller;

import com.demo.Blog.request.PostRequest;
import com.demo.Blog.request.PostUpdateRequest;
import com.demo.Blog.response.PostResponse;
import com.demo.Blog.response.PostUpdateResponse;
import com.demo.Blog.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts(@RequestParam Optional<Long> userId) {
        List<PostResponse> postResponses = postService.getAllPosts(userId);
        return ResponseEntity.ok(postResponses);
    }

    @PostMapping
    public ResponseEntity<PostResponse> createPost(@RequestBody PostRequest newPost) {
        PostResponse postResponse = postService.createPost(newPost);
        return new ResponseEntity<>(postResponse, HttpStatus.CREATED);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponse> getOnePost(@PathVariable Long postId) {
        PostResponse postResponse = postService.getOnePostById(postId);
        return ResponseEntity.ok(postResponse);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<PostUpdateResponse> updatePost(@PathVariable Long postId, @RequestBody PostUpdateRequest postUpdateRequest) {
        PostUpdateResponse postResponse = postService.updatePost(postId, postUpdateRequest);
        return ResponseEntity.ok(postResponse);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deletePostById(@PathVariable Long postId) {
        String message = postService.deletePostById(postId);
        return ResponseEntity.ok(message);
    }


    @GetMapping("/month")
    public ResponseEntity<List<PostResponse>> getAllThisMonthPosts() {
        List<PostResponse> postResponses = postService.getAllThisMonthPosts();
        return ResponseEntity.ok(postResponses);
    }

    @GetMapping("/category")
    public ResponseEntity<List<PostResponse>> getPostsByTag(@RequestParam("tag") String tag) {
        List<PostResponse> postResponses = postService.getPostByTag(tag);
        return ResponseEntity.ok(postResponses);
    }
}
