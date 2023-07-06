package com.demo.Blog.controller;

import com.demo.Blog.config.auth.JwtAuthenticationFilter;
import com.demo.Blog.request.CommentRequest;
import com.demo.Blog.request.CommentUpdateRequest;
import com.demo.Blog.response.CommentResponse;
import com.demo.Blog.service.CommentService;
import com.demo.Blog.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.core.IsNull;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@WebMvcTest(CommentController.class)
@AutoConfigureMockMvc(addFilters = false)
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private JwtService jwtService;
    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @MockBean
    private CommentService commentService;
    private ObjectMapper mapper = new ObjectMapper();


    @Test
    void it_should_get_all_comments() throws Exception {

        //given
        Mockito.when(commentService.getAllCommentsWithParam(Optional.empty(), Optional.empty()))
                .thenReturn(getAllCommentResponses());

        // when
        ResultActions resultActions = mockMvc.perform(get("/comments"));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].postId").value(2))
                .andExpect(jsonPath("$[0].userId").value(2))
                .andExpect(jsonPath("$[0].text").value("test"))
                .andExpect(jsonPath("$[0].createDate").value(LocalDateTime.MAX.toString()))
                .andExpect(jsonPath("$[0].updateDate").value(IsNull.nullValue()));
    }

    @Test
    void it_should_create_comment() throws Exception {

        // given
        Mockito.when(commentService.createComment(Mockito.any(CommentRequest.class))).thenReturn(getCommentResponse());

        String request = mapper.writeValueAsString(getCommentRequest());

        // when
        ResultActions resultActions = mockMvc.perform(post("/comments")
                .content(request).contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.postId").value(2))
                .andExpect(jsonPath("$.userId").value(2))
                .andExpect(jsonPath("$.text").value("test"))
                .andExpect(jsonPath("$.createDate").value(LocalDateTime.MAX.toString()))
                .andExpect(jsonPath("$.updateDate").value(IsNull.nullValue()));
    }

    @Test
    void it_should_get_one_comment() throws Exception {

        // given
        Mockito.when(commentService.getOneComment(Mockito.any(Long.class))).thenReturn(getCommentResponse());

        // when
        ResultActions resultActions = mockMvc.perform(get("/comments/1"));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.postId").value(2))
                .andExpect(jsonPath("$.userId").value(2))
                .andExpect(jsonPath("$.text").value("test"))
                .andExpect(jsonPath("$.createDate").value(LocalDateTime.MAX.toString()))
                .andExpect(jsonPath("$.updateDate").value(IsNull.nullValue()));
    }

    @Test
    void it_should_updateComment() throws Exception {

        //given
        Mockito.when(commentService.updateComment(Mockito.any(Long.class), Mockito.any(CommentUpdateRequest.class)))
                                .thenReturn(getUpdatedCommentResponse());

        String request = mapper.writeValueAsString(getCommentUpdateRequest());

        // when
        ResultActions resultActions = mockMvc.perform(put("/comments/1")
                .content(request).contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.postId").value(2))
                .andExpect(jsonPath("$.userId").value(2))
                .andExpect(jsonPath("$.text").value("test updated"))
                .andExpect(jsonPath("$.createDate").value(LocalDateTime.MAX.toString()))
                .andExpect(jsonPath("$.updateDate").value(LocalDateTime.MAX.toString()));

    }

    @Test
    void it_should_delete_comment() throws Exception {

        // given
        Mockito.when(commentService.deleteCommentById(Mockito.any(Long.class))).thenReturn(getDeleteMessage(1L));

        // when
        ResultActions resultActions = mockMvc.perform(delete("/comments/1"));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$").value(1));

    }

    private String getDeleteMessage(Long id) {
       return id.toString();
    }

    private CommentResponse getUpdatedCommentResponse() {
        return new CommentResponse(1L, 2L, 2L, "test updated",
                LocalDateTime.MAX, LocalDateTime.MAX);
    }

    private CommentUpdateRequest getCommentUpdateRequest() {
        return new CommentUpdateRequest("test updated");
    }

    private CommentRequest getCommentRequest() {
        return new CommentRequest(2L, 2L, "test");
    }

    private List<CommentResponse> getAllCommentResponses() {
        return List.of(getCommentResponse());
    }

    private CommentResponse getCommentResponse() {
        return new CommentResponse(1L, 2L, 2L, "test",
                LocalDateTime.MAX, null);
    }




}