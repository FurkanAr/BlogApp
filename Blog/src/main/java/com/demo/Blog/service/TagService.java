package com.demo.Blog.service;

import com.demo.Blog.converter.TagConverter;
import com.demo.Blog.exception.messages.Messages;
import com.demo.Blog.exception.tag.TagAlreadyInUseException;
import com.demo.Blog.exception.tag.TagNotFoundException;
import com.demo.Blog.exception.tag.TagNotFoundGivenTagNameException;
import com.demo.Blog.model.Tag;
import com.demo.Blog.repository.TagRepository;
import com.demo.Blog.request.TagRequest;
import com.demo.Blog.response.TagResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TagService {

    private final TagRepository tagRepository;
    private final TagConverter tagConverter;

    Logger logger = LoggerFactory.getLogger(getClass());

    public TagService(TagRepository tagRepository, TagConverter tagConverter) {
        this.tagRepository = tagRepository;
        this.tagConverter = tagConverter;
    }

    @Transactional
    public TagResponse createTag(TagRequest newTag) {
        logger.info("createTag method started");
        logger.info("Tag request: {} ", newTag);

        Optional<Tag> foundTag = tagRepository.findByName(newTag.getName());
        if (foundTag.isPresent()) {
            logger.warn("Tag exist: {} ", newTag);
            throw new TagAlreadyInUseException(Messages.TAG.EXIST + newTag);
        }
        Tag tag = tagRepository.save(tagConverter.convert(newTag));
        logger.info("Tag created: {} ", tag.getId());

        logger.info("createTag method successfully worked");
        return tagConverter.convert(tag);
    }


    public List<TagResponse> getAllTags() {
        logger.info("getAllTags method started");
        List<Tag> tags = tagRepository.findAll();
        logger.info("getAllTags method successfully worked");
        return tagConverter.convert(tags);

    }

    protected List<Tag> findAllById(List<Long> tagIds) {
        logger.info("findAllById method started");

        tagIds.forEach(id -> {
            if (!tagRepository.existsById(id)) {
                logger.warn("Tag not found: {} ", id);
                throw new TagNotFoundException(Messages.TAG.NOT_EXISTS_BY_ID + id);
            }
        });
        List<Tag> tags = tagRepository.findAllById(tagIds);
        logger.info("Tags found: {} ", tagIds);

        logger.info("findAllById method successfully worked");
        return tags;
    }


    protected Long findByName(String tag) {
        logger.info("findByName method started");

        Tag foundTag = tagRepository.findByName(tag).orElseThrow(() ->
                new TagNotFoundGivenTagNameException(Messages.TAG.NOT_EXISTS_BY_NAME + tag));

        logger.info("Found tagId: {} , by tag: {} ", foundTag.getId(), tag);

        logger.info("findByName method successfully worked");
        return foundTag.getId();
    }
}
