package com.demo.Blog.service;

import com.demo.Blog.converter.PostConverter;
import com.demo.Blog.exception.membership.MembershipIsExpiredException;
import com.demo.Blog.exception.post.PostNotFoundException;
import com.demo.Blog.exception.post.UserHasMaximumNumberOfPostException;
import com.demo.Blog.model.Membership;
import com.demo.Blog.model.Post;
import com.demo.Blog.model.Tag;
import com.demo.Blog.model.User;
import com.demo.Blog.model.enums.MembershipStatus;
import com.demo.Blog.model.enums.UserRole;
import com.demo.Blog.repository.PostRepository;
import com.demo.Blog.request.PostRequest;
import com.demo.Blog.request.PostUpdateRequest;
import com.demo.Blog.response.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
class PostServiceTest {

    @InjectMocks
    private PostService postService;
    @Mock
    private PostRepository postRepository;
    @Mock
    private PostConverter postConverter;
    @Mock
    private MembershipService membershipService;
    @Mock
    private TagService tagService;

    @Test
    void it_should_throw_membership_is_expired_exception() {

        // given
        Membership membership = getDeactivateMembership();
        membership.setId(1L);
        User user = getUser();
        user.setId(1L);
        membership.setUser(user);

        Mockito.when(membershipService.getMembershipByUserId(Mockito.anyLong())).thenReturn(membership);

        // when
        Throwable exception = catchThrowable(() -> postService.createPost(getPostRequest()));

        //then
        assertThat(exception).isInstanceOf(MembershipIsExpiredException.class);
        verify(postRepository, times(0)).save(Mockito.any(Post.class));
    }

    @Test
    void it_should_throw_user_has_maximum_number_of_post_exception() {

        // given
        Mockito.when(membershipService.getMembershipByUserId(Mockito.anyLong())).thenReturn(getUpdatedMembership());
        Mockito.when(tagService.findAllById(Mockito.anyList())).thenReturn(getTags());
        Mockito.when(postRepository.CountAllByPublicationDateBetweenByUserId(Mockito.anyLong(), Mockito.any(LocalDate.class), Mockito.any(LocalDate.class)))
                .thenReturn(10);
        // when
        Throwable exception = catchThrowable(() -> postService.createPost(getPostRequest()));

        //then
        assertThat(exception).isInstanceOf(UserHasMaximumNumberOfPostException.class);
        verify(postRepository, times(0)).save(Mockito.any(Post.class));
    }

    @Test
    void it_should_create_post() {

        // given
        Post post = getPost();
        post.setId(1L);
        Mockito.when(membershipService.getMembershipByUserId(Mockito.anyLong())).thenReturn(getUpdatedMembership());
        Mockito.when(tagService.findAllById(Mockito.anyList())).thenReturn(getTags());
        Mockito.when(postRepository.CountAllByPublicationDateBetweenByUserId(Mockito.anyLong(), Mockito.any(LocalDate.class), Mockito.any(LocalDate.class))).thenReturn(getPosts().size());
        Mockito.when(postConverter.convert(Mockito.any(PostRequest.class), Mockito.any(User.class), Mockito.anyList())).thenReturn(new Post());
        Mockito.when(postRepository.save(Mockito.any(Post.class))).thenReturn(post);
        Mockito.when(postConverter.convert(Mockito.any(Post.class))).thenReturn(getPostResponse(1L));

        // when
        PostResponse postResponse = postService.createPost(getPostRequest());

        //then
        assertThat(postResponse).isNotNull();
        assertEquals(postResponse.getId(), post.getId());
        verify(postRepository, times(1)).save(Mockito.any(Post.class));
    }

    @Test
    void it_should_get_all_posts_by_userId() {

        // given
        Mockito.when(postRepository.findByUserId(Mockito.anyLong())).thenReturn(getPosts());
        Mockito.when(postConverter.convert(Mockito.anyList()))
                    .thenReturn(getAllPostResponses());

        // when
        List<PostResponse> postResponses = postService.getAllPosts(Optional.of(1L));

        // then
        assertThat(postResponses).isNotNull();
        assertThat(postResponses.size()).isEqualTo(getPosts().size());
        assertEquals(postResponses.get(0).getUserId(), 1L);
    }

    @Test
    void it_should_get_all_posts() {

        // given
        Mockito.when(postRepository.findByOrderByPublicationDateDesc()).thenReturn(getPosts());
        Mockito.when(postConverter.convert(Mockito.anyList())).thenReturn(getAllPostResponses());

        // when
        List<PostResponse> postResponses = postService.getAllPosts(Optional.empty());

        // then
        assertThat(postResponses).isNotNull();
        assertThat(postResponses.size()).isEqualTo(getPosts().size());
    }

    @Test
    void it_should_get_all_this_month_posts() {

        // given
        Mockito.when(postRepository.findAllByPublicationDateBetween(Mockito.any(LocalDate.class),Mockito.any(LocalDate.class)))
                .thenReturn(getPosts());
        Mockito.when(postConverter.convert(Mockito.anyList())).thenReturn(getAllPostResponses());

        // when
        List<PostResponse> postResponses = postService.getAllThisMonthPosts();

        // then
        assertThat(postResponses).isNotNull();
        assertThat(postResponses.size()).isEqualTo(getPosts().size());
    }

    @Test
    void it_should_throw_post_not_found_exception(){

        // given

        // when
        Throwable exception = catchThrowable(() -> postService.getOnePostById(1L));

        //then
        assertThat(exception).isInstanceOf(PostNotFoundException.class);

    }

    @Test
    void it_should_get_one_post_by_id() {

        // given
        Post post = getPost();
        post.setId(1L);
        Mockito.when(postRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(post));
        Mockito.when(postConverter.convert(Mockito.any(Post.class))).thenReturn(getPostResponse(1L));

        // when
        PostResponse postResponse = postService.getOnePostById(1L);

        //then
        assertThat(postResponse).isNotNull();
        assertEquals(postResponse.getId(), 1L);
    }

    @Test
    void it_should_throw_post_not_found_exception_update_post(){

        // given

        // when
        Throwable exception = catchThrowable(() -> postService.updatePost(1L, getPostUpdateRequest()));

        //then
        assertThat(exception).isInstanceOf(PostNotFoundException.class);
    }

    @Test
    void it_should_update_post() {

        // given
        Post post = getPost();
        post.setId(1L);
        Mockito.when(postRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(post));
        Mockito.when(postConverter.update(Mockito.any(Post.class), Mockito.any(PostUpdateRequest.class)))
                .thenReturn(new Post());
        Mockito.when(postRepository.save(Mockito.any(Post.class))).thenReturn(post);
        Mockito.when(postConverter.update(Mockito.any(Post.class))).thenReturn(getPostUpdateResponse());

        // when
        PostUpdateResponse postUpdateResponse = postService.updatePost(1L, getPostUpdateRequest());

        // then
        assertThat(postUpdateResponse).isNotNull();
        assertEquals(postUpdateResponse.getId(), post.getId());
        assertEquals(postUpdateResponse.getText(), getPostUpdateRequest().getText());
        assertEquals(postUpdateResponse.getTitle(), getPostUpdateRequest().getTitle());

        verify(postRepository, times(1)).save(Mockito.any(Post.class));
    }

    @Test
    void it_should_throw_post_not_found_exception_get_post_by_id(){

        // given

        // when
        Throwable exception = catchThrowable(() -> postService.getPostById(1L));

        //then
        assertThat(exception).isInstanceOf(PostNotFoundException.class);
    }

    @Test
    void it_should_get_post_by_id() {

        // given
        Post post = getPost();
        post.setId(1L);
        Mockito.when(postRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(post));

        // when
        Post response = postService.getPostById(1L);

        //then
        assertThat(response).isNotNull();
        assertEquals(response.getId(), 1L);
    }

    @Test
    void it_should_throw_post_not_found_exception_delete_post_by_id(){

        // given

        // when
        Throwable exception = catchThrowable(() -> postService.deletePostById(1L));

        //then
        assertThat(exception).isInstanceOf(PostNotFoundException.class);
    }

    @Test
    void it_should_delete_post_by_id() {

        // given
        Post post = getPost();
        post.setId(1L);
        Mockito.when(postRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(post));

        // when
        String response = postService.deletePostById(1L);

        //then
        assertThat(response).isNotNull();
        assertEquals(response, String.valueOf(1));
        Mockito.verify(postRepository, times(1)).delete(Mockito.any(Post.class));
    }

    @Test
    void it_should_get_post_by_tag() {

        // given
        Mockito.when(tagService.findByName(Mockito.anyString())).thenReturn(1L);
        Mockito.when(postRepository.findPostsByTagsId(Mockito.anyLong())).thenReturn(getPosts());
        Mockito.when(postConverter.convert(Mockito.anyList())).thenReturn(getAllPostResponses());

        // when
        List<PostResponse> postResponses = postService.getPostByTag("web");

        // then
        assertThat(postResponses).isNotNull();
        assertThat(postResponses.size()).isEqualTo(getPosts().size());
    }

    private PostRequest getPostRequest() {
        return new PostRequest("test title", "test text", "test-picture.png", 1L, List.of(1L, 2L));
    }

    private Membership getDeactivateMembership() {
        return new Membership(LocalDate.now(Clock.systemDefaultZone()), LocalDate.now(Clock.systemDefaultZone()), getUser(), MembershipStatus.ACTIVE);
    }

    private Membership getMembership() {
        return new Membership(LocalDate.now(Clock.systemDefaultZone()), LocalDate.now(Clock.systemDefaultZone()).plusMonths(1), getUser(), MembershipStatus.ACTIVE);
    }

    private User getUser() {
        return new User("tester", "test-user", "test@gmail.com", "Test-password123", UserRole.USER);
    }

    private Membership getUpdatedMembership() {
        Membership membership = getMembership();
        membership.setId(1L);
        User user = getUser();
        user.setId(1L);
        membership.setUser(user);
        return membership;
    }

    private List<Tag> getTags() {
        return List.of(getTag("web"), getTag("programming"));
    }

    private Tag getTag(String name) {
        return new Tag(name);
    }

    private List<Post> getPosts() {
        return List.of(getPost(), getPost());
    }

    private Post getPost() {
        return new Post("test title", "test text", "test-picture.png", getUser());
    }

    private List<PostResponse> getAllPostResponses() {
        return List.of(getPostResponse(1L), getPostResponse(2L));
    }

    private PostResponse getPostResponse(Long id) {
        return new PostResponse(id, "test title", "test text", LocalDate.now(Clock.systemDefaultZone()),
                LocalDate.now(Clock.systemDefaultZone()), "test-picture.png", 1L,
                getAllCommentResponses(), getAllLikeResponses(), getAllTagResponses());
    }

    private PostUpdateRequest getPostUpdateRequest() {
        return new PostUpdateRequest("test updated title", "test updated text","test-picture.png");
    }

    private PostUpdateResponse getPostUpdateResponse() {
        return new PostUpdateResponse(1L, "test updated title", "test updated text", LocalDate.now(Clock.systemDefaultZone()),
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