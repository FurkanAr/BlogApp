package com.demo.Blog.controller;

import com.demo.Blog.client.model.enums.PaymentStatus;
import com.demo.Blog.client.model.enums.PaymentType;
import com.demo.Blog.client.request.PaymentCardGetRequest;
import com.demo.Blog.client.request.PaymentTransferGetRequest;
import com.demo.Blog.client.request.RenewMembershipCardRequest;
import com.demo.Blog.client.response.PaymentResponse;
import com.demo.Blog.config.auth.JwtAuthenticationFilter;
import com.demo.Blog.model.enums.MembershipStatus;
import com.demo.Blog.response.MembershipResponse;
import com.demo.Blog.service.JwtService;
import com.demo.Blog.service.MembershipService;
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

import java.time.Clock;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(MembershipController.class)
@AutoConfigureMockMvc(addFilters = false)
class MembershipControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private JwtService jwtService;
    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @MockBean
    private MembershipService membershipService;
    private ObjectMapper mapper = new ObjectMapper();

    @Test
    void it_should_start_membership_pay_by_card() throws Exception {

        // given
        Mockito.when(membershipService.createMembershipPayByCard(Mockito.any(PaymentCardGetRequest.class)))
                .thenReturn(getPaymentCardResponse());

        String request = mapper.writeValueAsString(getPaymentCardRequest());

        // when
        ResultActions resultActions = mockMvc.perform(post("/memberships/start/card")
                .content(request).contentType(MediaType.APPLICATION_JSON));

        //then
        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.message").value("test message"))
                .andExpect(jsonPath("$.userId").value(2))
                .andExpect(jsonPath("$.paymentType").value(PaymentType.CARD.toString()))
                .andExpect(jsonPath("$.status").value(PaymentStatus.ACCEPTED.toString()));
    }

    @Test
    void it_should_start_membership_pay_by_transfer() throws Exception {

        // given
        Mockito.when(membershipService.createMembershipPayByTransfer(Mockito.any(PaymentTransferGetRequest.class)))
                .thenReturn(getPaymentTransferResponse());

        String request = mapper.writeValueAsString(getPaymentTransferRequest());

        // when
        ResultActions resultActions = mockMvc.perform(post("/memberships/start/transfer")
                .content(request).contentType(MediaType.APPLICATION_JSON));

        //then
        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.message").value("test message"))
                .andExpect(jsonPath("$.userId").value(2))
                .andExpect(jsonPath("$.paymentType").value(PaymentType.TRANSFER.toString()))
                .andExpect(jsonPath("$.status").value(PaymentStatus.ACCEPTED.toString()));
    }

    @Test
    void it_should_renew_membership_card_request() throws Exception {

        // given
        Mockito.when(membershipService.renewMembershipCardRequest(Mockito.any(RenewMembershipCardRequest.class)))
                .thenReturn(getPaymentCardResponse());

        String request = mapper.writeValueAsString(getRenewMembershipCardRequest());

        // when
        ResultActions resultActions = mockMvc.perform(post("/memberships/renew/card")
                .content(request).contentType(MediaType.APPLICATION_JSON));

        //then
        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.message").value("test message"))
                .andExpect(jsonPath("$.userId").value(2))
                .andExpect(jsonPath("$.paymentType").value(PaymentType.CARD.toString()))
                .andExpect(jsonPath("$.status").value(PaymentStatus.ACCEPTED.toString()));
    }

    @Test
    void it_should_renew_membership_transfer_request() throws Exception {

        // given
        Mockito.when(membershipService.renewMembershipTransferRequest(Mockito.any(PaymentTransferGetRequest.class)))
                .thenReturn(getPaymentTransferResponse());

        String request = mapper.writeValueAsString(getPaymentTransferRequest());

        // when
        ResultActions resultActions = mockMvc.perform(post("/memberships/renew/transfer")
                .content(request).contentType(MediaType.APPLICATION_JSON));

        //then
        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.message").value("test message"))
                .andExpect(jsonPath("$.userId").value(2))
                .andExpect(jsonPath("$.paymentType").value(PaymentType.TRANSFER.toString()))
                .andExpect(jsonPath("$.status").value(PaymentStatus.ACCEPTED.toString()));
    }

    @Test
    void it_should_get_all_memberships() throws Exception {

        // given
        Mockito.when(membershipService.getAllMemberships())
                .thenReturn(getAllMembershipResponses());

        // when
        ResultActions resultActions = mockMvc.perform(get("/memberships"));

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
        Mockito.when(membershipService.getOneMembership(Mockito.any(Long.class)))
                .thenReturn(getMembershipResponse(1L));

        // when
        ResultActions resultActions = mockMvc.perform(get("/memberships/1"));

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.startDate").value(LocalDate.now(Clock.systemDefaultZone()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
                .andExpect(jsonPath("$.expireDate").value(LocalDate.now(Clock.systemDefaultZone()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
                .andExpect(jsonPath("$.userId").value(2L))
                .andExpect(jsonPath("$.status").value(MembershipStatus.ACTIVE.toString()));
    }

    @Test
    void it_should_delete_membership_by_id() throws Exception {

        // given
        Mockito.when(membershipService.deleteMembershipById(Mockito.any(Long.class)))
                .thenReturn(getDeleteMessage(1L));

        // when
        ResultActions resultActions = mockMvc.perform(delete("/memberships/1"));

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$").value(1));

    }

    @Test
    void getUserMembership() throws Exception {

        // given
        Mockito.when(membershipService.getUserMembership(Mockito.any(Long.class)))
                .thenReturn(getMembershipResponse(1L));

        // when
        ResultActions resultActions = mockMvc.perform(get("/memberships/users?userId=1"));

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.startDate").value(LocalDate.now(Clock.systemDefaultZone()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
                .andExpect(jsonPath("$.expireDate").value(LocalDate.now(Clock.systemDefaultZone()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
                .andExpect(jsonPath("$.userId").value(2L))
                .andExpect(jsonPath("$.status").value(MembershipStatus.ACTIVE.toString()));

    }

    private String getDeleteMessage(Long id) {
        return id.toString();
    }

    private List<MembershipResponse> getAllMembershipResponses() {
        return List.of(getMembershipResponse(1L), getMembershipResponse(2L));
    }

    private MembershipResponse getMembershipResponse(Long id) {
        return new MembershipResponse(id, LocalDate.now(Clock.systemDefaultZone()),
                LocalDate.now(Clock.systemDefaultZone()), 2L, MembershipStatus.ACTIVE);
    }

    private RenewMembershipCardRequest getRenewMembershipCardRequest() {
        return new RenewMembershipCardRequest(1L, 2L);
    }

    private PaymentTransferGetRequest getPaymentTransferRequest() {
        return new PaymentTransferGetRequest("TR 000212564899147532", "TR 090002562311458900", "MEDIUM", 2L);
    }

    private PaymentCardGetRequest getPaymentCardRequest() {
        return new PaymentCardGetRequest("4441113332", LocalDate.now(Clock.systemDefaultZone()), "131", 2L);
    }

    private PaymentResponse getPaymentCardResponse() {
        return new PaymentResponse(1L, "test message", 2L, PaymentType.CARD, PaymentStatus.ACCEPTED);
    }

    private PaymentResponse getPaymentTransferResponse() {
        return new PaymentResponse(2L, "test message", 2L, PaymentType.TRANSFER, PaymentStatus.ACCEPTED);
    }

}