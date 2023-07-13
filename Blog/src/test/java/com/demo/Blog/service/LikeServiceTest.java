package com.demo.Blog.service;

import com.demo.Blog.converter.LikeConverter;
import com.demo.Blog.exception.like.LikeNotFoundException;
import com.demo.Blog.exception.like.UserAlreadyLikedException;
import com.demo.Blog.exception.membership.MembershipIsExpiredException;
import com.demo.Blog.model.*;
import com.demo.Blog.model.enums.MembershipStatus;
import com.demo.Blog.model.enums.UserRole;
import com.demo.Blog.repository.LikeRepository;
import com.demo.Blog.request.LikeRequest;
import com.demo.Blog.response.LikeResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Clock;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
class LikeServiceTest {

    @InjectMocks
    private LikeService likeService;
    @Mock
    private LikeRepository likeRepository;
    @Mock
    private LikeConverter likeConverter;
    @Mock
    private MembershipService membershipService;
    @Mock
    private PostService postService;
    @Mock
    private UserService userService;

    @Test
    void it_should_get_all_likes_by_user_id_and_post_id() {

        // given
        Mockito.when(likeRepository.findByUserIdAndPostId(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(getAllLikes());
        Mockito.when(likeConverter.convert(Mockito.anyList())).thenReturn(getAllLikeResponses());

        // when
        List<LikeResponse> likeResponses = likeService
                .getAllLikesWithParam(Optional.of(2L), Optional.of(2L));

        // then
        assertThat(likeResponses).isNotNull();
        assertEquals(likeResponses.get(0).getId(), 1L);
        assertEquals(likeResponses.get(1).getId(), 2L);
        assertEquals(likeResponses.get(0).getUserId(), 2L);
        assertEquals(likeResponses.get(0).getPostId(), 2L);
    }

    @Test
    void it_should_get_all_likes_by_user_id() {

        // given
        Mockito.when(likeRepository.findByUserId(Mockito.anyLong()))
                .thenReturn(getAllLikes());
        Mockito.when(likeConverter.convert(Mockito.anyList())).thenReturn(getAllLikeResponses());

        // when
        List<LikeResponse> likeResponses = likeService
                .getAllLikesWithParam(Optional.of(2L), Optional.empty());

        // then
        assertThat(likeResponses).isNotNull();
        assertEquals(likeResponses.get(0).getId(), 1L);
        assertEquals(likeResponses.get(1).getId(), 2L);
        assertEquals(likeResponses.get(0).getUserId(), 2L);
        assertEquals(likeResponses.get(1).getUserId(), 2L);
    }

    @Test
    void it_should_get_all_likes_by_post_id() {

        // given
        Mockito.when(likeRepository.findByPostId(Mockito.anyLong()))
                .thenReturn(getAllLikes());
        Mockito.when(likeConverter.convert(Mockito.anyList())).thenReturn(getAllLikeResponses());

        // when
        List<LikeResponse> likeResponses = likeService
                .getAllLikesWithParam(Optional.empty(), Optional.of(2L));

        // then
        assertThat(likeResponses).isNotNull();
        assertEquals(likeResponses.get(0).getId(), 1L);
        assertEquals(likeResponses.get(1).getId(), 2L);
        assertEquals(likeResponses.get(0).getPostId(), 2L);
        assertEquals(likeResponses.get(1).getPostId(), 2L);
    }

    @Test
    void it_should_get_all_likes_with_param() {

        // given
        Mockito.when(likeRepository.findAll()).thenReturn(getAllLikes());
        Mockito.when(likeConverter.convert(Mockito.anyList())).thenReturn(getAllLikeResponses());

        // when
        List<LikeResponse> likeResponses = likeService
                .getAllLikesWithParam(Optional.empty(), Optional.empty());

        // then
        assertThat(likeResponses).isNotNull();
        assertEquals(likeResponses.get(0).getId(), 1L);
        assertEquals(likeResponses.get(1).getId(), 2L);
        assertEquals(likeResponses.get(0).getPostId(), 2L);
        assertEquals(likeResponses.get(1).getPostId(), 2L);
        assertEquals(likeResponses.get(0).getUserId(), 2L);
        assertEquals(likeResponses.get(1).getUserId(), 2L);
    }

    @Test
    void it_should_throw_membership_is_expired_exception() {

        // given
        Membership membership = getDeactivateMembership();
        membership.setId(1L);
        User user = getUser();
        user.setId(2L);
        membership.setUser(user);
        Post post = getPost();
        post.setId(2L);

        Mockito.when(userService.findUserById(Mockito.anyLong())).thenReturn(user);
        Mockito.when(postService.getPostById(Mockito.anyLong())).thenReturn(post);
        Mockito.when(membershipService.getMembershipByUserId(Mockito.anyLong())).thenReturn(membership);

        // when
        Throwable exception = catchThrowable(() -> likeService.createLike(getLikeRequest()));

        //then
        assertThat(exception).isInstanceOf(MembershipIsExpiredException.class);
        verify(likeRepository, times(0)).save(Mockito.any(Like.class));
    }

    @Test
    void it_should_throw_user_already_liked_exception() {

        // given
        Membership membership = getMembership();
        membership.setId(1L);
        User user = getUser();
        user.setId(2L);
        membership.setUser(user);
        Post post = getPost();
        post.setId(2L);
        Like like = getLike();
        like.setId(1L);
        like.setUser(user);
        post.setLikeList(Collections.singletonList(like));

        Mockito.when(userService.findUserById(Mockito.anyLong())).thenReturn(user);
        Mockito.when(postService.getPostById(Mockito.anyLong())).thenReturn(post);
        Mockito.when(membershipService.getMembershipByUserId(Mockito.anyLong())).thenReturn(membership);

        // when
        Throwable exception = catchThrowable(() -> likeService.createLike(getLikeRequest()));

        //then
        assertThat(exception).isInstanceOf(UserAlreadyLikedException.class);
        verify(likeRepository, times(0)).save(Mockito.any(Like.class));
    }

    @Test
    void createLike() {

        // given
        Membership membership = getMembership();
        membership.setId(1L);
        User user = getUser();
        user.setId(2L);
        membership.setUser(user);
        Post post = getPost();
        post.setId(2L);
        Like like = getLike();
        like.setId(1L);
        post.setLikeList(Collections.singletonList(like));

        Mockito.when(userService.findUserById(Mockito.anyLong())).thenReturn(user);
        Mockito.when(postService.getPostById(Mockito.anyLong())).thenReturn(post);
        Mockito.when(membershipService.getMembershipByUserId(Mockito.anyLong())).thenReturn(membership);
        Mockito.when(likeConverter.convert(Mockito.any(Post.class), Mockito.any(User.class)))
                        .thenReturn(new Like());
        Mockito.when(likeRepository.save(Mockito.any(Like.class))).thenReturn(like);
        Mockito.when(likeConverter.convert(Mockito.any(Like.class))).thenReturn(getLikeResponse(1L));

        // when
        LikeResponse likeResponse = likeService.createLike(getLikeRequest());

        //then
        assertThat(likeResponse).isNotNull();
        assertEquals(likeResponse.getId(), 1L);
        assertEquals(likeResponse.getUserId(), 2L);
        assertEquals(likeResponse.getPostId(), 2L);
        verify(likeRepository, times(1)).save(Mockito.any(Like.class));
    }

    @Test
    void it_should_throw_like_not_found_exception(){

        // given

        // when
        Throwable exception = catchThrowable(() -> likeService.getOneLike(1L));

        assertThat(exception).isInstanceOf(LikeNotFoundException.class);
    }

    @Test
    void it_should_get_one_like() {

        // given
        Like like = getLike();
        like.setId(1L);
        Mockito.when(likeRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(like));
        Mockito.when(likeConverter.convert(Mockito.any(Like.class))).thenReturn(getLikeResponse(1L));

        // when
        LikeResponse likeResponse = likeService.getOneLike(1L);

        //then
        assertThat(likeResponse).isNotNull();
        assertEquals(likeResponse.getId(), 1L);
        assertEquals(likeResponse.getUserId(), 2L);
        assertEquals(likeResponse.getPostId(), 2L);
    }

    @Test
    void it_should_throw_like_not_found_exception_delete_by_id(){

        // given

        // when
        Throwable exception = catchThrowable(() -> likeService.deleteLikeById(1L));

        assertThat(exception).isInstanceOf(LikeNotFoundException.class);
        verify(likeRepository, times(0)).delete(Mockito.any(Like.class));
    }

    @Test
    void it_should_delete_like_by_id() {

        // given
        Like like = getLike();
        like.setId(1L);
        Mockito.when(likeRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(like));

        // when
        String response = likeService.deleteLikeById(1L);

        //then
        assertThat(response).isNotNull();
        assertEquals(response, String.valueOf(1L));
        verify(likeRepository, times(1)).delete(Mockito.any(Like.class));
    }

    @Test
    void it_should_throw_like_not_found_exception_get_by_like_id(){

        // given

        // when
        Throwable exception = catchThrowable(() -> likeService.getLikeById(1L));

        assertThat(exception).isInstanceOf(LikeNotFoundException.class);
    }

    @Test
    void it_should_get_like_by_id() {

        // given
        Like like = getLike();
        like.setId(1L);
        Mockito.when(likeRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(like));

        // when
        Like response = likeService.getLikeById(1L);

        //then
        assertThat(response).isNotNull();
        assertEquals(response.getId(), 1L);
    }

    private List<Like> getAllLikes() {
        return List.of(getLike(), getLike());
    }

    private Like getLike() {
        return new Like(getPost(), getUser());
    }

    private Post getPost() {
        return new Post("test title", "test text", "test-picture.png", getUser());
    }

    private User getUser() {
        return new User("tester", "test-user", "test@gmail.com", "Test-password123", UserRole.USER);
    }

    private LikeRequest getLikeRequest() {
        return new LikeRequest(2L, 2L);
    }

    private List<LikeResponse> getAllLikeResponses() {
        return List.of(getLikeResponse(1L), getLikeResponse(2L));
    }

    private LikeResponse getLikeResponse(Long id) {
        return new LikeResponse(id, 2L, 2L);
    }

    private Membership getDeactivateMembership() {
        return new Membership(LocalDate.now(Clock.systemDefaultZone()), LocalDate.now(Clock.systemDefaultZone()), getUser(), MembershipStatus.ACTIVE);
    }

    private Membership getMembership() {
        return new Membership(LocalDate.now(Clock.systemDefaultZone()), LocalDate.now(Clock.systemDefaultZone()).plusMonths(1), getUser(), MembershipStatus.ACTIVE);
    }
}