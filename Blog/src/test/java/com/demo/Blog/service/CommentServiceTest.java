package com.demo.Blog.service;

import com.demo.Blog.converter.CommentConverter;
import com.demo.Blog.exception.comment.CommentNotFoundException;
import com.demo.Blog.exception.membership.MembershipIsExpiredException;
import com.demo.Blog.model.Comment;
import com.demo.Blog.model.Membership;
import com.demo.Blog.model.Post;
import com.demo.Blog.model.User;
import com.demo.Blog.model.enums.MembershipStatus;
import com.demo.Blog.model.enums.UserRole;
import com.demo.Blog.repository.CommentRepository;
import com.demo.Blog.request.CommentRequest;
import com.demo.Blog.request.CommentUpdateRequest;
import com.demo.Blog.response.CommentResponse;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
class CommentServiceTest {

    @InjectMocks
    private CommentService commentService;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private CommentConverter commentConverter;
    @Mock
    private MembershipService membershipService;
    @Mock
    private PostService postService;

    @Test
    void it_should_get_all_comments_by_user_id_and_post_id() {

        // given
        Mockito.when(commentRepository.findByUserIdAndPostId(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(getComments());
        Mockito.when(commentConverter.convert(Mockito.anyList())).thenReturn(getAllCommentResponses());

        // when
        List<CommentResponse> commentResponses = commentService.getAllCommentsWithParam(Optional.of(2L), Optional.of(2L));

        // given
        assertThat(commentResponses).isNotNull();
        assertEquals(commentResponses.get(0).getId(), 1L);
        assertEquals(commentResponses.get(0).getUserId(), 2L);
        assertEquals(commentResponses.get(0).getPostId(), 2L);
    }


    @Test
    void it_should_get_all_comments_by_user_id() {

        // given
        Mockito.when(commentRepository.findByUserId(Mockito.anyLong()))
                .thenReturn(getComments());
        Mockito.when(commentConverter.convert(Mockito.anyList())).thenReturn(getAllCommentResponses());

        // when
        List<CommentResponse> commentResponses = commentService.getAllCommentsWithParam(Optional.of(2L), Optional.empty());

        // given
        assertThat(commentResponses).isNotNull();
        assertEquals(commentResponses.get(0).getId(), 1L);
        assertEquals(commentResponses.get(0).getUserId(), 2L);
        assertEquals(commentResponses.get(0).getPostId(), 2L);
    }

    @Test
    void it_should_get_all_comments_by_post_id() {

        // given
        Mockito.when(commentRepository.findByPostId(Mockito.anyLong()))
                .thenReturn(getComments());
        Mockito.when(commentConverter.convert(Mockito.anyList())).thenReturn(getAllCommentResponses());

        // when
        List<CommentResponse> commentResponses = commentService.getAllCommentsWithParam(Optional.empty(), Optional.of(2L));

        // given
        assertThat(commentResponses).isNotNull();
        assertEquals(commentResponses.get(0).getId(), 1L);
        assertEquals(commentResponses.get(0).getUserId(), 2L);
        assertEquals(commentResponses.get(0).getPostId(), 2L);
    }

    @Test
    void it_should_get_all_comments() {
        // given
        Mockito.when(commentRepository.findAll()).thenReturn(getComments());
        Mockito.when(commentConverter.convert(Mockito.anyList())).thenReturn(getAllCommentResponses());

        // when
        List<CommentResponse> commentResponses = commentService.getAllCommentsWithParam(Optional.empty(), Optional.empty());

        // given
        assertThat(commentResponses).isNotNull();
        assertEquals(commentResponses.get(0).getId(), 1L);
        assertEquals(commentResponses.get(0).getUserId(), 2L);
        assertEquals(commentResponses.get(0).getPostId(), 2L);
    }

    @Test
    void it_should_throw_comment_not_found_exception() {

        // given

        // when
        Throwable exception = catchThrowable(() -> commentService.getOneComment(1L));

        //then
        assertThat(exception).isInstanceOf(CommentNotFoundException.class);
    }

    @Test
    void it_should_get_one_comment() {

        // given
        Comment comment = getComment();
        comment.setId(1L);
        Mockito.when(commentRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(comment));
        Mockito.when(commentConverter.convert(Mockito.any(Comment.class))).thenReturn(getCommentResponse());

        // when
        CommentResponse commentResponse = commentService.getOneComment(1L);

        // then
        assertThat(commentResponse).isNotNull();
        assertEquals(commentResponse.getId(), 1L);
        assertEquals(commentResponse.getId(), comment.getId());
    }

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
        Throwable exception = catchThrowable(() -> commentService.createComment(getCommentRequest()));

        //then
        assertThat(exception).isInstanceOf(MembershipIsExpiredException.class);
        verify(commentRepository, times(0)).save(Mockito.any(Comment.class));
    }

    @Test
    void it_should_create_comment() {

        // given
        Membership membership = getMembership();
        membership.setId(1L);
        User user = getUser();
        user.setId(2L);
        membership.setUser(user);
        Post post = getPost();
        post.setId(2L);
        Comment comment = getComment();
        comment.setId(1L);

        Mockito.when(membershipService.getMembershipByUserId(Mockito.anyLong())).thenReturn(membership);
        Mockito.when(postService.getPostById(Mockito.anyLong())).thenReturn(post);
        Mockito.when(commentConverter.convert(Mockito.any(CommentRequest.class),
                        Mockito.any(Post.class), Mockito.any(User.class)))
                .thenReturn(new Comment());
        Mockito.when(commentRepository.save(Mockito.any(Comment.class))).thenReturn(comment);
        Mockito.when(commentConverter.convert(Mockito.any(Comment.class))).thenReturn(getCommentResponse());
        // when

        CommentResponse commentResponse = commentService.createComment(getCommentRequest());

        //then
        assertThat(commentResponse).isNotNull();
        assertEquals(commentResponse.getId(), 1L);
        assertEquals(commentResponse.getText(), getCommentRequest().getText());
        verify(commentRepository, times(1)).save(Mockito.any(Comment.class));
    }

    @Test
    void it_should_throw_comment_not_found_exception_update_comment() {

        // given

        // when
        Throwable exception = catchThrowable(() -> commentService.updateComment(1L, getCommentUpdateRequest()));

        //then
        assertThat(exception).isInstanceOf(CommentNotFoundException.class);
        verify(commentRepository, times(0)).save(Mockito.any(Comment.class));
    }

    @Test
    void it_should_update_comment() {

        // given
        Comment comment = getComment();
        comment.setId(1L);
        Mockito.when(commentRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(comment));
        Mockito.when(commentRepository.save(Mockito.any(Comment.class))).thenReturn(comment);
        Mockito.when(commentConverter.convert(Mockito.any(Comment.class))).thenReturn(getUpdatedCommentResponse());

        // when
        CommentResponse commentResponse = commentService.updateComment(1L, getCommentUpdateRequest());

        // then
        assertThat(commentResponse).isNotNull();
        assertEquals(commentResponse.getText(), getCommentUpdateRequest().getText());
        verify(commentRepository, times(1)).save(Mockito.any(Comment.class));
    }

    @Test
    void it_should_throw_comment_not_found_exception_delete_comment() {

        // given

        // when
        Throwable exception = catchThrowable(() -> commentService.deleteCommentById(1L));

        //then
        assertThat(exception).isInstanceOf(CommentNotFoundException.class);
        verify(commentRepository, times(0)).delete(Mockito.any(Comment.class));
    }

    @Test
    void it_should_delete_comment_by_id() {

        // given
        Comment comment = getComment();
        comment.setId(1L);
        Mockito.when(commentRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(comment));

        // when
        String response = commentService.deleteCommentById(1L);

        // then
        assertThat(response).isNotNull();
        assertEquals(response, String.valueOf(1L));
        verify(commentRepository, times(1)).delete(Mockito.any(Comment.class));
    }

    @Test
    void it_should_throw_comment_not_found_exception_get_comment_by_id() {

        // given

        // when
        Throwable exception = catchThrowable(() -> commentService.getCommentById(1L));

        //then
        assertThat(exception).isInstanceOf(CommentNotFoundException.class);
    }

    @Test
    void it_should_get_comment_by_id() {

        // given
        Comment comment = getComment();
        comment.setId(1L);
        Mockito.when(commentRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(comment));

        // when
        Comment response = commentService.getCommentById(1L);

        // then
        assertThat(response).isNotNull();
        assertEquals(response.getId(), 1L);
    }

    private List<Comment> getComments() {
        return List.of(getComment());
    }

    private Comment getComment() {
        return new Comment(getPost(), getUser(), "test text");
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

    private Post getPost() {
        return new Post("test title", "test text", "test-picture.png", getUser());
    }

    private User getUser() {
        return new User("tester", "test-user", "test@gmail.com", "Test-password123", UserRole.USER);
    }

    private Membership getDeactivateMembership() {
        return new Membership(LocalDate.now(Clock.systemDefaultZone()), LocalDate.now(Clock.systemDefaultZone()), getUser(), MembershipStatus.ACTIVE);
    }

    private Membership getMembership() {
        return new Membership(LocalDate.now(Clock.systemDefaultZone()), LocalDate.now(Clock.systemDefaultZone()).plusMonths(1), getUser(), MembershipStatus.ACTIVE);
    }
}