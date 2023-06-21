package com.demo.Blog.converter;

import com.demo.Blog.model.Tag;
import com.demo.Blog.request.TagRequest;
import com.demo.Blog.response.TagResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TagConverter {

    public Tag convert(TagRequest tagRequest){
        Tag tag = new Tag();
        tag.setName(tagRequest.getName());
        return tag;
    }

    public TagResponse convert(Tag tag){
        TagResponse tagResponse = new TagResponse();
        tagResponse.setId(tag.getId());
        tagResponse.setName(tag.getName());
        return tagResponse;
    }

    public List<TagResponse> convert(List<Tag> tagList){
        List<TagResponse> tagResponses = new ArrayList<>();
        tagList.stream().forEach(tag -> tagResponses.add(convert(tag)));
        return tagResponses;
    }

}
