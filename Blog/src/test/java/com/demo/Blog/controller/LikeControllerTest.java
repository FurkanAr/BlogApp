package com.demo.Blog.controller;

import com.demo.Blog.config.auth.JwtAuthenticationFilter;
import com.demo.Blog.request.LikeRequest;
import com.demo.Blog.response.LikeResponse;
import com.demo.Blog.service.JwtService;
import com.demo.Blog.service.LikeService;
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
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(LikeController.class)
@AutoConfigureMockMvc(addFilters = false)
class LikeControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private JwtService jwtService;
    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @MockBean
    private LikeService likeService;
    private ObjectMapper mapper = new ObjectMapper();


    @Test
    void it_should_get_all_likes() throws Exception {

        //given
        Mockito.when(likeService.getAllLikesWithParam(Optional.empty(), Optional.empty()))
                .thenReturn(getAllLikeResponses());

        // when
        ResultActions resultActions = mockMvc.perform(get("/likes"));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].postId").value(2))
                .andExpect(jsonPath("$[0].userId").value(2));
    }

    @Test
    void it_should_create_like() throws Exception {

        //given
        Mockito.when(likeService.createLike(Mockito.any(LikeRequest.class))).thenReturn(getLikeResponse());

        String request = mapper.writeValueAsString(getLikeRequest());

        // when
        ResultActions resultActions = mockMvc.perform(post("/likes")
                .content(request).contentType(MediaType.APPLICATION_JSON));

        //then
        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.postId").value(2))
                .andExpect(jsonPath("$.userId").value(2));
    }

    @Test
    void it_should_get_one_like() throws Exception {

        //given
        Mockito.when(likeService.getOneLike(Mockito.any(Long.class)))
                .thenReturn(getLikeResponse());

        // when
        ResultActions resultActions = mockMvc.perform(get("/likes/1"));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.postId").value(2))
                .andExpect(jsonPath("$.userId").value(2));
    }

    @Test
    void it_should_delete_like() throws Exception {

        //given
        Mockito.when(likeService.deleteLikeById(Mockito.any(Long.class)))
                .thenReturn(getDeleteMessage(1L));

        // when
        ResultActions resultActions = mockMvc.perform(delete("/likes/1"));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$").value(1));
    }

    private String getDeleteMessage(Long id) {
        return id.toString();
    }

    private LikeRequest getLikeRequest() {
        return new LikeRequest(2L, 2L);
    }

    private List<LikeResponse> getAllLikeResponses() {
        return List.of(getLikeResponse());
    }

    private LikeResponse getLikeResponse() {
        return new LikeResponse(1L, 2L, 2L);
    }

}