package com.demo.Blog.converter;

import com.demo.Blog.model.Tag;
import com.demo.Blog.request.TagRequest;
import com.demo.Blog.response.TagResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TagConverter {

    Logger logger = LoggerFactory.getLogger(getClass());

    public TagResponse convert(Tag tag) {
        logger.info("convert to Response method started");
        TagResponse tagResponse = new TagResponse();
        tagResponse.setId(tag.getId());
        tagResponse.setName(tag.getName());
        logger.info("convert to Response method successfully worked");
        return tagResponse;
    }

    public Tag convert(TagRequest tagRequest) {
        logger.info("convert to Tag method started");
        Tag tag = new Tag();
        tag.setName(tagRequest.getName());
        logger.info("convert to Tag method successfully worked");
        return tag;
    }

    public List<TagResponse> convert(List<Tag> tagList) {
        logger.info("convert tagList to tagResponses method started");
        List<TagResponse> tagResponses = new ArrayList<>();
        tagList.forEach(tag -> tagResponses.add(convert(tag)));
        logger.info("convert tagList to tagResponses method successfully worked");
        return tagResponses;
    }

}
