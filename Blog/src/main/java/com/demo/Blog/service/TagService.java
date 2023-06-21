package com.demo.Blog.service;

import com.demo.Blog.converter.TagConverter;
import com.demo.Blog.model.Post;
import com.demo.Blog.model.Tag;
import com.demo.Blog.repository.TagRepository;
import com.demo.Blog.request.TagRequest;
import com.demo.Blog.response.TagResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TagService {

    private final TagRepository tagRepository;
    private final MembershipService membershipService;
    private final TagConverter tagConverter;


    public TagService(TagRepository tagRepository, MembershipService membershipService, TagConverter tagConverter) {
        this.tagRepository = tagRepository;
        this.membershipService = membershipService;
        this.tagConverter = tagConverter;
    }

    public TagResponse createTag(TagRequest tagRequest) {
        Optional<Tag> foundTag = tagRepository.findByName(tagRequest.getName());
        if (foundTag.isPresent()){
            throw new RuntimeException("tag already created");
        }
        Tag tag = tagRepository.save(tagConverter.convert(tagRequest));
        return tagConverter.convert(tag);
    }


    public List<TagResponse> getAllTags() {
        return tagConverter.convert(tagRepository.findAll());

    }

    public List<Tag> findAllById(List<Long> tagIds) {
        tagIds.stream().forEach(id -> {
            if(!tagRepository.existsById(id)){
                throw new RuntimeException("Not found Tag  with id = " + id);
            }
        });
       return tagRepository.findAllById(tagIds);
    }


    public Long findByName(String tag) {
        Tag foundTag = tagRepository.findByName(tag).orElseThrow(() -> new RuntimeException("tag not found"));
        return foundTag.getId();
    }
}
