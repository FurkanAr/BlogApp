package com.demo.Blog.controller;


import com.demo.Blog.client.model.enums.PaymentStatus;
import com.demo.Blog.client.model.enums.PaymentType;
import com.demo.Blog.client.response.PaymentResponse;
import com.demo.Blog.config.auth.JwtAuthenticationFilter;
import com.demo.Blog.model.enums.MembershipStatus;
import com.demo.Blog.model.enums.UserRole;
import com.demo.Blog.response.*;
import com.demo.Blog.service.DashboardService;
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
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@WebMvcTest(AdminDashboardController.class)
@AutoConfigureMockMvc(addFilters = false)
class AdminDashboardControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private JwtService jwtService;
    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @MockBean
    private DashboardService dashboardService;
    private ObjectMapper mapper = new ObjectMapper();


    @Test
    void it_should_get_all_users() throws Exception {

        // given
        Mockito.when(dashboardService.getAllUsers()).thenReturn(getAllUserResponses());

        // when
        ResultActions resultActions = mockMvc.perform(get("/dashboard/users"));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].userName").value("tester"))
                .andExpect(jsonPath("$[0].fullName").value("test-user"))
                .andExpect(jsonPath("$[0].email").value("test@gmail.com"))
                .andExpect(jsonPath("$[0].role").value(UserRole.USER.toString()));
    }

    @Test
    void it_should_get_user() throws Exception {
        // given
        Mockito.when(dashboardService.getUserById(Mockito.any(Long.class))).thenReturn(getUserResponse());

        // when
        ResultActions resultActions = mockMvc.perform(get("/dashboard/users/1"));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.userName").value("tester"))
                .andExpect(jsonPath("$.fullName").value("test-user"))
                .andExpect(jsonPath("$.email").value("test@gmail.com"))
                .andExpect(jsonPath("$.role").value(UserRole.USER.toString()));
    }

    @Test
    void it_should_get_total_number_of_user() throws Exception {

        // given
        Mockito.when(dashboardService.getTotalNumberOfUser()).thenReturn(getNumberOfUser());

        // when
        ResultActions resultActions = mockMvc.perform(get("/dashboard/users/total"));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(1000L));
    }

    @Test
    void it_should_get_all_posts() throws Exception {

        //given
        Mockito.when(dashboardService.getAllPosts(Optional.empty())).thenReturn(getAllPostResponses());

        // when
        ResultActions resultActions = mockMvc.perform(get("/dashboard/posts"));

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
    void it_should_get_all_memberships() throws Exception {

        // given
        Mockito.when(dashboardService.getAllMemberships())
                .thenReturn(getAllMembershipResponses());

        // when
        ResultActions resultActions = mockMvc.perform(get("/dashboard/memberships"));

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].startDate").value(LocalDate.now(Clock.systemDefaultZone()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
                .andExpect(jsonPath("$[0].expireDate").value(LocalDate.now(Clock.systemDefaultZone()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
                .andExpect(jsonPath("$[0].userId").value(2L))
                .andExpect(jsonPath("$[0].status").value(MembershipStatus.ACTIVE.toString()))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].startDate").value(LocalDate.now(Clock.systemDefaultZone()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
                .andExpect(jsonPath("$[1].expireDate").value(LocalDate.now(Clock.systemDefaultZone()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
                .andExpect(jsonPath("$[1].userId").value(2L))
                .andExpect(jsonPath("$[1].status").value(MembershipStatus.ACTIVE.toString()));
    }

    @Test
    void it_should_get_one_membership() throws Exception {

        // given
        Mockito.when(dashboardService.getOneMembership(Mockito.any(Long.class)))
                .thenReturn(getMembershipResponse(1L));

        // when
        ResultActions resultActions = mockMvc.perform(get("/dashboard/memberships/1"));

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.startDate").value(LocalDate.now(Clock.systemDefaultZone()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
                .andExpect(jsonPath("$.expireDate").value(LocalDate.now(Clock.systemDefaultZone()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
                .andExpect(jsonPath("$.userId").value(2L))
                .andExpect(jsonPath("$.status").value(MembershipStatus.ACTIVE.toString()));
    }

    @Test
    void it_should_get_total_number_0f_memberships() throws Exception {

        // given
        Mockito.when(dashboardService.getTotalNumberOfMemberships()).thenReturn(getNumberOfMemberships());

        // when
        ResultActions resultActions = mockMvc.perform(get("/dashboard/memberships/total"));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(500L));
    }

    @Test
    void it_should_get_all_payments() throws Exception {

        // given
        Mockito.when(dashboardService.getAllPayments()).thenReturn(getAllPaymentResponses());

        // when
        ResultActions resultActions = mockMvc.perform(get("/dashboard/payments"));

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].message").value("test message"))
                .andExpect(jsonPath("$[0].userId").value(2))
                .andExpect(jsonPath("$[0].paymentType").value(PaymentType.CARD.toString()))
                .andExpect(jsonPath("$[0].status").value(PaymentStatus.ACCEPTED.toString()));
    }

    @Test
    void it_should_get_one_payment() throws Exception {

        // given
        Mockito.when(dashboardService.getOnePayment(Mockito.any(Long.class)))
                .thenReturn(getPaymentResponse());

        // when
        ResultActions resultActions = mockMvc.perform(get("/dashboard/payments/1"));

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.message").value("test message"))
                .andExpect(jsonPath("$.userId").value(2))
                .andExpect(jsonPath("$.paymentType").value(PaymentType.CARD.toString()))
                .andExpect(jsonPath("$.status").value(PaymentStatus.ACCEPTED.toString()));
    }

    @Test
    void it_should_get_total_payment_price() throws Exception {

        // given
        Mockito.when(dashboardService.getTotalPaymentPrice()).thenReturn(getTotalPayment());

        // when
        ResultActions resultActions = mockMvc.perform(get("/dashboard/payments/total"));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(30000));
    }

    @Test
    void it_should_get_one_user_payments() throws Exception {

        // given
        Mockito.when(dashboardService.getOneUserPayments(Mockito.any(Long.class))).thenReturn(getOneUserTotalPayment());

        // when
        ResultActions resultActions = mockMvc.perform(get("/dashboard/payments/users/1"));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(139.5));
    }

    private BigDecimal getOneUserTotalPayment() {
        return new BigDecimal("139.5");
    }

    private Long getNumberOfUser() {
        return 1000L;
    }

    private Long getNumberOfMemberships() {
        return 500L;
    }

    private BigDecimal getTotalPayment() {
        return new BigDecimal("30000");
    }

    private List<UserResponse> getAllUserResponses() {
        return List.of(getUserResponse());
    }

    private UserResponse getUserResponse() {
        return new UserResponse(1L, "tester", "test-user", "test@gmail.com", UserRole.USER);
    }

    private List<PaymentResponse> getAllPaymentResponses() {
        return List.of(getPaymentResponse());
    }

    private PaymentResponse getPaymentResponse() {
        return new PaymentResponse(1L, "test message", 2L, PaymentType.CARD, PaymentStatus.ACCEPTED);
    }

    private List<MembershipResponse> getAllMembershipResponses() {
        return List.of(getMembershipResponse(1L), getMembershipResponse(2L));
    }

    private MembershipResponse getMembershipResponse(Long id) {
        return new MembershipResponse(id, LocalDate.now(Clock.systemDefaultZone()),
                LocalDate.now(Clock.systemDefaultZone()), 2L, MembershipStatus.ACTIVE);
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