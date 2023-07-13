package com.demo.Blog.service;

import com.demo.Blog.client.PaymentServiceClient;
import com.demo.Blog.client.model.enums.PaymentStatus;
import com.demo.Blog.client.model.enums.PaymentType;
import com.demo.Blog.client.response.PaymentResponse;
import com.demo.Blog.model.Membership;
import com.demo.Blog.model.Post;
import com.demo.Blog.model.User;
import com.demo.Blog.model.enums.MembershipStatus;
import com.demo.Blog.model.enums.UserRole;
import com.demo.Blog.response.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class DashboardServiceTest {

    @InjectMocks
    private DashboardService dashboardService;
    @Mock
    private  UserService userService;
    @Mock
    private  PostService postService;
    @Mock
    private  MembershipService membershipService;
    @Mock
    private  PaymentServiceClient paymentServiceClient;

    @Test
    void it_should_get_all_users() {

        // given
        Mockito.when(userService.findAll()).thenReturn(getAllUserResponses());

        //when
        List<UserResponse> userResponses = dashboardService.getAllUsers();

        // then
        assertThat(userResponses).isNotNull();
        assertThat(userResponses.get(0).getUserName()).isEqualTo(getUser().getUserName());
        assertThat(userResponses.get(0).getEmail()).isEqualTo(getUser().getEmail());
        assertThat(userResponses.get(0).getRole()).isEqualTo(getUser().getRole());
    }

    @Test
    void it_should_get_by_user_id() {

        // given
        Mockito.when(userService.findById(Mockito.anyLong())).thenReturn(getUserResponse());

        // when
        UserResponse userResponse = dashboardService.getUserById(1L);

        // then
        assertThat(userResponse).isNotNull();
        assertThat(userResponse.getUserName()).isEqualTo(getUser().getUserName());
        assertThat(userResponse.getEmail()).isEqualTo(getUser().getEmail());
        assertThat(userResponse.getRole()).isEqualTo(getUser().getRole());
    }

    @Test
    void it_should_get_total_number_of_user() {

        // given
        Mockito.when(userService.findNumberOfUsers()).thenReturn((long) getUsers().size());

        // when
        Long total = dashboardService.getTotalNumberOfUser();

        // then
        assertThat(total).isNotNull();
        assertThat(total).isEqualTo(getUsers().size());
    }

    @Test
    void it_should_get_all_posts_by_userId() {

        // given
        Mockito.when(postService.getAllPosts(Optional.of(1L))).thenReturn(getAllPostResponses());

        // when
        List<PostResponse> postResponses = dashboardService.getAllPosts(Optional.of(1L));

        // then
        assertThat(postResponses).isNotNull();
        assertEquals(postResponses.size(), getPosts().size());
        assertEquals(postResponses.get(0).getUserId(), 1L);
    }

    @Test
    void it_should_get_all_posts() {

        // given
        Mockito.when(postService.getAllPosts(Optional.empty())).thenReturn(getAllPostResponses());

        // when
        List<PostResponse> postResponses = postService.getAllPosts(Optional.empty());

        // then
        assertThat(postResponses).isNotNull();
        assertThat(postResponses.size()).isEqualTo(getPosts().size());
    }

    @Test
    void it_should_get_all_memberships() {

        // given
        Mockito.when(membershipService.getAllMemberships()).thenReturn(getAllMembershipResponses());

        // when
        List<MembershipResponse> membershipResponses = dashboardService.getAllMemberships();

        // then
        assertThat(membershipResponses).isNotNull();
        assertEquals(membershipResponses.get(0).getId(), 1L);
        assertEquals(membershipResponses.get(1).getId(), 2L);
        assertEquals(membershipResponses.get(0).getStatus(), getMemberships().get(0).getStatus());
        assertEquals(membershipResponses.get(1).getStatus(), getMemberships().get(1).getStatus());
    }

    @Test
    void it_should_get_one_membership() {

        // given

        Mockito.when(membershipService.getOneMembership(Mockito.anyLong())).thenReturn(getMembershipResponse(1L));

        // when
        MembershipResponse membershipResponse = dashboardService.getOneMembership(1L);

        // then
        assertThat(membershipResponse).isNotNull();
        assertEquals(membershipResponse.getId(), 1L);
    }

    @Test
    void it_should_get_total_number_of_memberships() {

        // given
        Mockito.when(membershipService.getTotalNumberOfMemberships()).thenReturn(Long.valueOf(getMemberships().size()));

        // when
        Long response = dashboardService.getTotalNumberOfMemberships();

        // then
        assertThat(response).isNotNull();
        assertEquals(response, getMemberships().size());
    }

    @Test
    void it_should_get_all_payments() {

        // given
        Mockito.when(paymentServiceClient.getAllPayments()).thenReturn(getPaymentResponses());

        // when
        List<PaymentResponse> paymentResponses = dashboardService.getAllPayments();

        // then
        assertThat(paymentResponses).isNotNull();
        assertEquals(paymentResponses.get(0).getId(), 1L);
        assertEquals(paymentResponses.get(1).getId(), 2L);
        assertEquals(paymentResponses.get(0).getUserId(), 2L);
        assertEquals(paymentResponses.get(1).getUserId(), 2L);
    }

    @Test
    void it_should_get_one_payment() {

        // given
        Mockito.when(paymentServiceClient.getOnePayment(Mockito.anyLong()))
                .thenReturn(getPaymentCardPaymentAcceptedResponse());

        // when
        PaymentResponse paymentResponse = dashboardService.getOnePayment(1L);

        // then
        assertThat(paymentResponse).isNotNull();
        assertEquals(paymentResponse.getId(), 1L);
        assertEquals(paymentResponse.getUserId(), 2L);
    }

    @Test
    void it_should_get_total_payment_price() {

        // given
        Mockito.when(paymentServiceClient.getTotalPrice())
                .thenReturn(BigDecimal.valueOf(3000));

        // when
        BigDecimal response = dashboardService.getTotalPaymentPrice();

        // then
        assertThat(response).isNotNull();
        assertEquals(response, BigDecimal.valueOf(3000));
    }

    @Test
    void getOneUserPayments() {

        // given
        Mockito.when(paymentServiceClient.getOneUserPayments(Mockito.anyLong()))
                .thenReturn(BigDecimal.valueOf(75.45));

        // when
        BigDecimal response  = dashboardService.getOneUserPayments(2L);

        // then
        assertThat(response).isNotNull();
        assertEquals(response, BigDecimal.valueOf(75.45));
    }

    private List<UserResponse> getAllUserResponses() {
        return List.of(getUserResponse());
    }

    private UserResponse getUserResponse() {
        return new UserResponse(1L, "tester", "test-user", "test@gmail.com", UserRole.USER);
    }

    private List<User> getUsers() {
        return List.of(getUser());
    }

    private User getUser() {
        return new User("tester", "test-user", "test@gmail.com", "Test-password123", UserRole.USER);
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

    private List<Membership> getMemberships() {
        return List.of(getMembership(), getMembership());
    }

    private Membership getMembership() {
        return new Membership(LocalDate.now(Clock.systemDefaultZone()), LocalDate.now(Clock.systemDefaultZone()), getUser(), MembershipStatus.ACTIVE);
    }

    private List<MembershipResponse> getAllMembershipResponses() {
        return List.of(getMembershipResponse(1L), getMembershipResponse(2L));
    }

    private MembershipResponse getMembershipResponse(Long id) {
        return new MembershipResponse(id, LocalDate.now(Clock.systemDefaultZone()),
                LocalDate.now(Clock.systemDefaultZone()), 1L, MembershipStatus.ACTIVE);
    }

    private List<PaymentResponse> getPaymentResponses(){
        return List.of(getPaymentCardPaymentAcceptedResponse(), getPaymentTransferAcceptedResponse());
    }

    private PaymentResponse getPaymentCardPaymentAcceptedResponse() {
        return new PaymentResponse(1L, "Payment successful, Enjoy!!", 2L, PaymentType.CARD, PaymentStatus.ACCEPTED);
    }

    private PaymentResponse getPaymentTransferAcceptedResponse() {
        return new PaymentResponse(2L, "Payment successful, Enjoy!!", 2L, PaymentType.TRANSFER, PaymentStatus.ACCEPTED);
    }
}