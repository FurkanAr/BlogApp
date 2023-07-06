package com.demo.Blog.controller;

import com.demo.Blog.config.auth.JwtAuthenticationFilter;
import com.demo.Blog.request.TagRequest;
import com.demo.Blog.response.TagResponse;
import com.demo.Blog.service.JwtService;
import com.demo.Blog.service.TagService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TagController.class)
@AutoConfigureMockMvc(addFilters = false)
class TagControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private JwtService jwtService;
    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @MockBean
    private TagService tagService;
    private ObjectMapper mapper = new ObjectMapper();


    @Test
    void it_should_get_all_tags() throws Exception {

        // given
        Mockito.when(tagService.getAllTags()).thenReturn(getTagResponses());

        // when
        ResultActions resultActions = mockMvc.perform(get("/tags"));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("web"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("programming"));
    }

    @Test
    void it_should_create_tag() throws Exception {

        // given
        Mockito.when(tagService.createTag(Mockito.any(TagRequest.class))).thenReturn(getTagResponse(1L, "web"));

        String request = mapper.writeValueAsString(getTagRequest());

        // when
        ResultActions resultActions = mockMvc.perform(post("/tags")
                .content(request).contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("web"));
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