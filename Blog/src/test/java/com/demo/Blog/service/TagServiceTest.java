package com.demo.Blog.service;

import com.demo.Blog.converter.TagConverter;
import com.demo.Blog.exception.tag.TagAlreadyInUseException;
import com.demo.Blog.exception.tag.TagNotFoundException;
import com.demo.Blog.exception.tag.TagNotFoundGivenTagNameException;
import com.demo.Blog.model.Tag;
import com.demo.Blog.repository.TagRepository;
import com.demo.Blog.request.TagRequest;
import com.demo.Blog.response.TagResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
class TagServiceTest {

    @InjectMocks
    private TagService tagService;
    @Mock
    private TagRepository tagRepository;
    @Mock
    private TagConverter tagConverter;

    @Test
    void it_should_tag_already_in_use_exception() {

        //given
        Mockito.when(tagRepository.findByName(Mockito.anyString())).thenReturn(Optional.of(getTag("web")));

        //when
        Throwable exception = catchThrowable(() -> tagService.createTag(getTagRequest()));

        // then
        assertThat(exception).isInstanceOf(TagAlreadyInUseException.class);
    }

    @Test
    void it_should_create_tag(){

        // given
        Mockito.when(tagRepository.findByName(Mockito.anyString())).thenReturn(Optional.empty());
        Mockito.when(tagConverter.convert(Mockito.any(TagRequest.class))).thenReturn(new Tag());
        Mockito.when(tagRepository.save(Mockito.any(Tag.class))).thenReturn(getTag("web"));
        Mockito.when(tagConverter.convert(Mockito.any(Tag.class))).thenReturn(getTagResponse(1L, "web"));

        // when
        TagResponse tagResponse = tagService.createTag(getTagRequest());

        // then
        assertThat(tagResponse).isNotNull();
        assertEquals(tagResponse.getName(), getTag("web").getName());
        verify(tagRepository).save(Mockito.any(Tag.class));
    }

    @Test
    void it_should_get_all_tags() {

        // given
        Mockito.when(tagRepository.findAll()).thenReturn(getTags());
        Mockito.when(tagConverter.convert(Mockito.anyList())).thenReturn(getTagResponses());

        // when
        List<TagResponse> tagResponses = tagService.getAllTags();

        // then
        assertThat(tagResponses).isNotNull();
        assertEquals(tagResponses.size(), getTags().size());
    }

    @Test
    void  it_should_throw_tag_not_found_exception(){

        //given
        Mockito.when(tagRepository.existsById(Mockito.anyLong())).thenReturn(false);

        //when
        Throwable exception = catchThrowable(() -> tagService.findAllById(List.of(1L,2L)));

        // then
        assertThat(exception).isInstanceOf(TagNotFoundException.class);
    }

    @Test
    void it_should_find_all_by_id() {

        // given
        Mockito.when(tagRepository.existsById(Mockito.anyLong())).thenReturn(true);
        Mockito.when(tagRepository.findAllById(Mockito.anyList())).thenReturn(getTags());

        // when
        List<Tag> tags = tagService.findAllById(List.of(1L,2L));

        // then
        assertThat(tags).isNotNull();
        assertEquals(tags.get(0).getName() , getTags().get(0).getName());
        assertEquals(tags.size(), getTags().size());
    }

    @Test
    void it_should_throw_tag_not_found_given_tag_name_exception() {

        //given

        //when
        Throwable exception = catchThrowable(() -> tagService.findByName("web"));

        // then
        assertThat(exception).isInstanceOf(TagNotFoundGivenTagNameException.class);
    }

    @Test
    void it_should_find_by_name(){

        // given
        Tag tag = getTag("web");
        tag.setId(1L);
        Mockito.when(tagRepository.findByName(Mockito.anyString())).thenReturn(Optional.of(tag));

        // when
        Long response = tagService.findByName("web");

        // then
        assertThat(response).isNotNull();
        assertEquals(response, 1L);
    }

    private Tag getTag(String name) {
        return new Tag(name);
    }

    private List<Tag> getTags(){
        return List.of(getTag( "web"), getTag( "programming"));
    }

    private List<TagResponse> getTagResponses() {
        return List.of(getTagResponse(1L, "web"), getTagResponse(2L, "programming"));
    }

    private TagResponse getTagResponse(Long id, String name) {
        return new TagResponse(id, name);
    }

    private TagRequest getTagRequest() {
        return new TagRequest("web");
    }
}