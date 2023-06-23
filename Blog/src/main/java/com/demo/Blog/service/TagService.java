package com.demo.Blog.service;

import com.demo.Blog.converter.TagConverter;
import com.demo.Blog.exception.tag.TagNotFoundGivenTagNameException;
import com.demo.Blog.exception.messages.Messages;
import com.demo.Blog.exception.tag.TagAlreadyInUseException;
import com.demo.Blog.exception.tag.TagNotFoundException;
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
            throw new TagAlreadyInUseException(Messages.TAG.EXIST + tagRequest);
        }
        Tag tag = tagRepository.save(tagConverter.convert(tagRequest));
        return tagConverter.convert(tag);
    }


    public List<TagResponse> getAllTags() {
        return tagConverter.convert(tagRepository.findAll());

    }

    protected List<Tag> findAllById(List<Long> tagIds) {
        tagIds.forEach(id -> {
            if(!tagRepository.existsById(id)){
                throw new TagNotFoundException(Messages.TAG.NOT_EXISTS_BY_ID + id);
            }
        });
       return tagRepository.findAllById(tagIds);
    }


    protected Long findByName(String tag) {
        Tag foundTag = tagRepository.findByName(tag).orElseThrow(() ->
                new TagNotFoundGivenTagNameException(Messages.TAG.NOT_EXISTS_BY_NAME + tag));
        return foundTag.getId();
    }
}
