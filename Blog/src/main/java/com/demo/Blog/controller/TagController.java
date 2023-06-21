package com.demo.Blog.controller;

import com.demo.Blog.request.TagRequest;
import com.demo.Blog.response.TagResponse;
import com.demo.Blog.service.TagService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tags")
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    public ResponseEntity<List<TagResponse>> getAllTags() {
        List<TagResponse> tagResponses = tagService.getAllTags();
        return ResponseEntity.ok(tagResponses);
    }

    @PostMapping
    public ResponseEntity<TagResponse> createTag(@RequestBody TagRequest tagRequest) {
        TagResponse tagResponse = tagService.createTag(tagRequest);
        return new ResponseEntity<>(tagResponse, HttpStatus.CREATED);
    }
}
