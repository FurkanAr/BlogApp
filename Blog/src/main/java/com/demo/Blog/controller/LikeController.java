package com.demo.Blog.controller;

import com.demo.Blog.request.LikeRequest;
import com.demo.Blog.response.LikeResponse;
import com.demo.Blog.service.LikeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/likes")
public class LikeController {

    private final LikeService likeService;
    Logger logger = LoggerFactory.getLogger(getClass());

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @GetMapping
    public ResponseEntity<List<LikeResponse>> getAllLikes(@RequestParam Optional<Long> userId,
                                                          @RequestParam Optional<Long> postId) {
        logger.debug("getAllLikes method started");
        List<LikeResponse> likeResponses = likeService.getAllLikesWithParam(userId, postId);
        logger.info("getAllLikes successfully worked");
        return ResponseEntity.ok(likeResponses);
    }

    @PostMapping
    public ResponseEntity<LikeResponse> createLike(@RequestBody @Valid LikeRequest likeRequest) {
        logger.debug("createLike method started");
        LikeResponse likeResponse = likeService.createLike(likeRequest);
        logger.info("createLike successfully worked, postId: {}, userId: {}",
                likeRequest.getPostId(), likeRequest.getUserId());
        return new ResponseEntity<>(likeResponse, HttpStatus.CREATED);
    }

    @GetMapping("/{likeId}")
    public ResponseEntity<LikeResponse> getOneLike(@PathVariable Long likeId) {
        logger.debug("getOneLike method started");
        LikeResponse likeResponse = likeService.getOneLike(likeId);
        logger.info("getOneLike successfully worked, likeId: {}", likeId);
        return ResponseEntity.ok(likeResponse);
    }

    @DeleteMapping("/{likeId}")
    public ResponseEntity<String> deleteLike(@PathVariable Long likeId) {
        logger.debug("deleteLike method started");
        String message = likeService.deleteLikeById(likeId);
        logger.info("deleteLike successfully worked, likeId: {}", likeId);
        return ResponseEntity.ok(message);
    }


}

