package com.demo.Blog.controller;

import com.demo.Blog.config.auth.JwtAuthenticationFilter;
import com.demo.Blog.request.PostRequest;
import com.demo.Blog.request.PostUpdateRequest;
import com.demo.Blog.response.*;
import com.demo.Blog.service.JwtService;
import com.demo.Blog.service.PostService;
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

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PostController.class)
@AutoConfigureMockMvc(addFilters = false)
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private JwtService jwtService;
    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @MockBean
    private PostService postService;

    private ObjectMapper mapper = new ObjectMapper();


    @Test
    void it_should_get_all_posts() throws Exception {

        //given
        Mockito.when(postService.getAllPosts(Optional.empty())).thenReturn(getAllPostResponses());

        // when
        ResultActions resultActions = mockMvc.perform(get("/posts"));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("test title"))
                .andExpect(jsonPath("$[0].text").value("test text"))
                .andExpect(jsonPath("$[0].publicationDate").value(LocalDate.now(Clock.systemDefaultZone()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
                .andExpect(jsonPath("$[0].updateDate").value(LocalDate.now(Clock.systemDefaultZone()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
                .andExpect(jsonPath("$[0].picture").value("test-picture.png"))
                .andExpect(jsonPath("$[0].userId").value(2))
                .andExpect(jsonPath("$[0].commentResponseList[0].id").value(1))
                .andExpect(jsonPath("$[0].commentResponseList[0].postId").value(2))
                .andExpect(jsonPath("$[0].commentResponseList[0].userId").value(2))
                .andExpect(jsonPath("$[0].commentResponseList[0].text").value("test"))
                .andExpect(jsonPath("$[0].commentResponseList[0].createDate").value(LocalDateTime.MAX.toString()))
                .andExpect(jsonPath("$[0].commentResponseList[0].updateDate").value(IsNull.nullValue()))
                .andExpect(jsonPath("$[0].likeResponseList[0].id").value(1))
                .andExpect(jsonPath("$[0].likeResponseList[0].postId").value(2))
                .andExpect(jsonPath("$[0].likeResponseList[0].userId").value(2))
                .andExpect(jsonPath("$[0].tagResponseList[0].id").value(1))
                .andExpect(jsonPath("$[0].tagResponseList[0].name").value("web"))
                .andExpect(jsonPath("$[0].tagResponseList[1].id").value(2))
                .andExpect(jsonPath("$[0].tagResponseList[1].name").value("programming"));
    }

    @Test
    void it_should_create_post() throws Exception {

        // given
        Mockito.when(postService.createPost(Mockito.any(PostRequest.class))).thenReturn(getPostResponse());

        String request = mapper.writeValueAsString(getPostRequest());

        // when
        ResultActions resultActions = mockMvc.perform(post("/posts")
                .content(request).contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("test title"))
                .andExpect(jsonPath("$.text").value("test text"))
                .andExpect(jsonPath("$.publicationDate").value(LocalDate.now(Clock.systemDefaultZone()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
                .andExpect(jsonPath("$.updateDate").value(LocalDate.now(Clock.systemDefaultZone()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
                .andExpect(jsonPath("$.picture").value("test-picture.png"))
                .andExpect(jsonPath("$.userId").value(2))
                .andExpect(jsonPath("$.commentResponseList[0].id").value(1))
                .andExpect(jsonPath("$.commentResponseList[0].postId").value(2))
                .andExpect(jsonPath("$.commentResponseList[0].userId").value(2))
                .andExpect(jsonPath("$.commentResponseList[0].text").value("test"))
                .andExpect(jsonPath("$.commentResponseList[0].createDate").value(LocalDateTime.MAX.toString()))
                .andExpect(jsonPath("$.commentResponseList[0].updateDate").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.likeResponseList[0].id").value(1))
                .andExpect(jsonPath("$.likeResponseList[0].postId").value(2))
                .andExpect(jsonPath("$.likeResponseList[0].userId").value(2))
                .andExpect(jsonPath("$.tagResponseList[0].id").value(1))
                .andExpect(jsonPath("$.tagResponseList[0].name").value("web"))
                .andExpect(jsonPath("$.tagResponseList[1].id").value(2))
                .andExpect(jsonPath("$.tagResponseList[1].name").value("programming"));
    }

    @Test
    void it_should_get_one_post() throws Exception {

        // given
        Mockito.when(postService.getOnePostById(Mockito.any(Long.class))).thenReturn(getPostResponse());

        // when
        ResultActions resultActions = mockMvc.perform(get("/posts/1"));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("test title"))
                .andExpect(jsonPath("$.text").value("test text"))
                .andExpect(jsonPath("$.publicationDate").value(LocalDate.now(Clock.systemDefaultZone()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
                .andExpect(jsonPath("$.updateDate").value(LocalDate.now(Clock.systemDefaultZone()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
                .andExpect(jsonPath("$.picture").value("test-picture.png"))
                .andExpect(jsonPath("$.userId").value(2))
                .andExpect(jsonPath("$.commentResponseList[0].id").value(1))
                .andExpect(jsonPath("$.commentResponseList[0].postId").value(2))
                .andExpect(jsonPath("$.commentResponseList[0].userId").value(2))
                .andExpect(jsonPath("$.commentResponseList[0].text").value("test"))
                .andExpect(jsonPath("$.commentResponseList[0].createDate").value(LocalDateTime.MAX.toString()))
                .andExpect(jsonPath("$.commentResponseList[0].updateDate").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.likeResponseList[0].id").value(1))
                .andExpect(jsonPath("$.likeResponseList[0].postId").value(2))
                .andExpect(jsonPath("$.likeResponseList[0].userId").value(2))
                .andExpect(jsonPath("$.tagResponseList[0].id").value(1))
                .andExpect(jsonPath("$.tagResponseList[0].name").value("web"))
                .andExpect(jsonPath("$.tagResponseList[1].id").value(2))
                .andExpect(jsonPath("$.tagResponseList[1].name").value("programming"));
    }

    @Test
    void it_should_update_post() throws Exception {
        // given
        Mockito.when(postService.updatePost(Mockito.any(Long.class), Mockito.any(PostUpdateRequest.class)))
                .thenReturn(getPostUpdateResponse());

        String request = mapper.writeValueAsString(getPostUpdateRequest());
        // when
        ResultActions resultActions = mockMvc.perform(put("/posts/1")
                .content(request).contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("test updated title"))
                .andExpect(jsonPath("$.text").value("test updated text"))
                .andExpect(jsonPath("$.publicationDate").value(LocalDate.now(Clock.systemDefaultZone()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
                .andExpect(jsonPath("$.updateDate").value(LocalDate.now(Clock.systemDefaultZone()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
                .andExpect(jsonPath("$.picture").value("test-picture.png"))
                .andExpect(jsonPath("$.userId").value(2))
                .andExpect(jsonPath("$.commentResponseList[0].id").value(1))
                .andExpect(jsonPath("$.commentResponseList[0].postId").value(2))
                .andExpect(jsonPath("$.commentResponseList[0].userId").value(2))
                .andExpect(jsonPath("$.commentResponseList[0].text").value("test"))
                .andExpect(jsonPath("$.commentResponseList[0].createDate").value(LocalDateTime.MAX.toString()))
                .andExpect(jsonPath("$.commentResponseList[0].updateDate").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.likeResponseList[0].id").value(1))
                .andExpect(jsonPath("$.likeResponseList[0].postId").value(2))
                .andExpect(jsonPath("$.likeResponseList[0].userId").value(2))
                .andExpect(jsonPath("$.tagResponseList[0].id").value(1))
                .andExpect(jsonPath("$.tagResponseList[0].name").value("web"))
                .andExpect(jsonPath("$.tagResponseList[1].id").value(2))
                .andExpect(jsonPath("$.tagResponseList[1].name").value("programming"));
    }

    @Test
    void it_should_delete_post_by_id() throws Exception {

        // given
        Mockito.when(postService.deletePostById(Mockito.any(Long.class)))
                .thenReturn(getDeleteMessage(1L));

        // when
        ResultActions resultActions = mockMvc.perform(delete("/posts/1"));

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$").value(1));

    }

    @Test
    void it_should_get_all_this_month_posts() throws Exception {

        //given
        Mockito.when(postService.getAllThisMonthPosts()).thenReturn(getAllPostResponses());

        // when
        ResultActions resultActions = mockMvc.perform(get("/posts/month"));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("test title"))
                .andExpect(jsonPath("$[0].text").value("test text"))
                .andExpect(jsonPath("$[0].publicationDate").value(LocalDate.now(Clock.systemDefaultZone()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
                .andExpect(jsonPath("$[0].updateDate").value(LocalDate.now(Clock.systemDefaultZone()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
                .andExpect(jsonPath("$[0].picture").value("test-picture.png"))
                .andExpect(jsonPath("$[0].userId").value(2))
                .andExpect(jsonPath("$[0].commentResponseList[0].id").value(1))
                .andExpect(jsonPath("$[0].commentResponseList[0].postId").value(2))
                .andExpect(jsonPath("$[0].commentResponseList[0].userId").value(2))
                .andExpect(jsonPath("$[0].commentResponseList[0].text").value("test"))
                .andExpect(jsonPath("$[0].commentResponseList[0].createDate").value(LocalDateTime.MAX.toString()))
                .andExpect(jsonPath("$[0].commentResponseList[0].updateDate").value(IsNull.nullValue()))
                .andExpect(jsonPath("$[0].likeResponseList[0].id").value(1))
                .andExpect(jsonPath("$[0].likeResponseList[0].postId").value(2))
                .andExpect(jsonPath("$[0].likeResponseList[0].userId").value(2))
                .andExpect(jsonPath("$[0].tagResponseList[0].id").value(1))
                .andExpect(jsonPath("$[0].tagResponseList[0].name").value("web"))
                .andExpect(jsonPath("$[0].tagResponseList[1].id").value(2))
                .andExpect(jsonPath("$[0].tagResponseList[1].name").value("programming"));
    }

    @Test
    void it_should_get_posts_by_tag() throws Exception {

        //given
        Mockito.when(postService.getPostByTag(Mockito.any(String.class))).thenReturn(getAllPostResponses());

        // when
        ResultActions resultActions = mockMvc.perform(get("/posts/category?tag=web"));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("test title"))
                .andExpect(jsonPath("$[0].text").value("test text"))
                .andExpect(jsonPath("$[0].publicationDate").value(LocalDate.now(Clock.systemDefaultZone()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
                .andExpect(jsonPath("$[0].updateDate").value(LocalDate.now(Clock.systemDefaultZone()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
                .andExpect(jsonPath("$[0].picture").value("test-picture.png"))
                .andExpect(jsonPath("$[0].userId").value(2))
                .andExpect(jsonPath("$[0].commentResponseList[0].id").value(1))
                .andExpect(jsonPath("$[0].commentResponseList[0].postId").value(2))
                .andExpect(jsonPath("$[0].commentResponseList[0].userId").value(2))
                .andExpect(jsonPath("$[0].commentResponseList[0].text").value("test"))
                .andExpect(jsonPath("$[0].commentResponseList[0].createDate").value(LocalDateTime.MAX.toString()))
                .andExpect(jsonPath("$[0].commentResponseList[0].updateDate").value(IsNull.nullValue()))
                .andExpect(jsonPath("$[0].likeResponseList[0].id").value(1))
                .andExpect(jsonPath("$[0].likeResponseList[0].postId").value(2))
                .andExpect(jsonPath("$[0].likeResponseList[0].userId").value(2))
                .andExpect(jsonPath("$[0].tagResponseList[0].id").value(1))
                .andExpect(jsonPath("$[0].tagResponseList[0].name").value("web"))
                .andExpect(jsonPath("$[0].tagResponseList[1].id").value(2))
                .andExpect(jsonPath("$[0].tagResponseList[1].name").value("programming"));
    }

    private String getDeleteMessage(Long id) {
        return id.toString();
    }
    private PostUpdateRequest getPostUpdateRequest() {
        return new PostUpdateRequest("test updated title", "test updated text ","test-picture.png");
    }

    private PostUpdateResponse getPostUpdateResponse() {
        return new PostUpdateResponse(1L, "test updated title", "test updated text", LocalDate.now(Clock.systemDefaultZone()),
                LocalDate.now(Clock.systemDefaultZone()), "test-picture.png", 2L,
                getAllCommentResponses(), getAllLikeResponses(), getAllTagResponses());
    }

    private PostRequest getPostRequest() {
        return new PostRequest("test title", "test text", "test-picture.png", 2L, List.of(1L,2L));
    }

    private List<PostResponse> getAllPostResponses() {
        return List.of(getPostResponse());
    }

    private PostResponse getPostResponse() {
        return new PostResponse(1L, "test title", "test text", LocalDate.now(Clock.systemDefaultZone()),
                LocalDate.now(Clock.systemDefaultZone()), "test-picture.png", 2L,
                getAllCommentResponses(), getAllLikeResponses(), getAllTagResponses());
    }

    private List<CommentResponse> getAllCommentResponses() {
        return List.of(getCommentResponse());
    }

    private CommentResponse getCommentResponse() {
        return new CommentResponse(1L, 2L, 2L, "test",
                LocalDateTime.MAX, null);
    }

    private List<LikeResponse> getAllLikeResponses() {
        return List.of(getLikeResponse());
    }

    private LikeResponse getLikeResponse() {
        return new LikeResponse(1L, 2L, 2L);
    }

    private List<TagResponse> getAllTagResponses() {
        return List.of(getTagResponse(1L, "web"), getTagResponse(2L, "programming"));
    }

    private TagResponse getTagResponse(Long id, String name) {
        return new TagResponse(id, name);
    }
}