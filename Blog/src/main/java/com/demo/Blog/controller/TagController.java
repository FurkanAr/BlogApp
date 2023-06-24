package com.demo.Blog.controller;

import com.demo.Blog.request.TagRequest;
import com.demo.Blog.response.TagResponse;
import com.demo.Blog.service.TagService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/tags")
public class TagController {

    private final TagService tagService;
    Logger logger = LoggerFactory.getLogger(getClass());

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    public ResponseEntity<List<TagResponse>> getAllTags() {
        logger.debug("getAllTags method started");
        List<TagResponse> tagResponses = tagService.getAllTags();
        logger.info("getAllTags successfully worked");
        return ResponseEntity.ok(tagResponses);
    }

    @PostMapping
    public ResponseEntity<TagResponse> createTag(@RequestBody @Valid TagRequest tagRequest) {
        logger.debug("createPost method started");
        TagResponse tagResponse = tagService.createTag(tagRequest);
        logger.info("createPost successfully worked, tagRequest: {}", tagRequest);
        return new ResponseEntity<>(tagResponse, HttpStatus.CREATED);
    }
}
