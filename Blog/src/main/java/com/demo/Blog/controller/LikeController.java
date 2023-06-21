package com.demo.Blog.controller;

import com.demo.Blog.request.LikeRequest;
import com.demo.Blog.response.LikeResponse;
import com.demo.Blog.service.LikeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/likes")
public class LikeController {

    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @GetMapping
    public ResponseEntity<List<LikeResponse>> getAllLikes(@RequestParam Optional<Long> userId,
                                                          @RequestParam Optional<Long> postId) {
        List<LikeResponse> likeResponses = likeService.getAllLikesWithParam(userId, postId);
        return ResponseEntity.ok(likeResponses);
    }

    @PostMapping
    public ResponseEntity<LikeResponse> createLike(@RequestBody LikeRequest likeRequest) {
        LikeResponse likeResponse = likeService.createLike(likeRequest);
        return new ResponseEntity<>(likeResponse, HttpStatus.CREATED);
    }

    @GetMapping("/{likeId}")
    public ResponseEntity<LikeResponse> getOneLike(@PathVariable Long likeId) {
        LikeResponse likeResponse = likeService.getOneLike(likeId);
        return ResponseEntity.ok(likeResponse);
    }

    @DeleteMapping("/{likeId}")
    public ResponseEntity<String> deleteLike(@PathVariable Long likeId) {
        String message = likeService.deleteLikeById(likeId);
        return ResponseEntity.ok(message);
    }


}

