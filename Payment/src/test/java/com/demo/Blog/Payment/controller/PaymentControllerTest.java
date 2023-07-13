package com.demo.Blog.Payment.controller;

import com.demo.Blog.Payment.model.enums.PaymentStatus;
import com.demo.Blog.Payment.model.enums.PaymentType;
import com.demo.Blog.Payment.request.PaymentCarSendRequest;
import com.demo.Blog.Payment.request.PaymentTransferSendRequest;
import com.demo.Blog.Payment.response.PaymentResponse;
import com.demo.Blog.Payment.service.PaymentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PaymentController.class)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PaymentService paymentService;

    private ObjectMapper mapper = new ObjectMapper();


    @Test
    void it_should_pay_by_card() throws Exception {

        // given
        Mockito.when(paymentService.createPayment(Mockito.any(PaymentCarSendRequest.class)))
                .thenReturn(getPaymentCardResponse());

        String request = mapper.writeValueAsString(getPaymentCarSendRequest());

        // when
        ResultActions resultActions = mockMvc.perform(post("/payments/card")
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
    void it_should_pay_by_transfer() throws Exception {

        // given
        Mockito.when(paymentService.createPayment(Mockito.any(PaymentTransferSendRequest.class)))
                .thenReturn(getPaymentTransferResponse());

        String request = mapper.writeValueAsString(getPaymentTransferSendRequest());

        // when
        ResultActions resultActions = mockMvc.perform(post("/payments/transfer")
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
    void it_should_get_all_payments() throws Exception {

        // given
        Mockito.when(paymentService.getAllPayments()).thenReturn(getAllPaymentResponses());

        // when
        ResultActions resultActions = mockMvc.perform(get("/payments"));

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].message").value("test message"))
                .andExpect(jsonPath("$[0].userId").value(2))
                .andExpect(jsonPath("$[0].paymentType").value(PaymentType.CARD.toString()))
                .andExpect(jsonPath("$[0].status").value(PaymentStatus.ACCEPTED.toString()))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].message").value("test message"))
                .andExpect(jsonPath("$[1].userId").value(2))
                .andExpect(jsonPath("$[1].paymentType").value(PaymentType.TRANSFER.toString()))
                .andExpect(jsonPath("$[1].status").value(PaymentStatus.ACCEPTED.toString()));
    }

    @Test
    void it_should_get_one_payment() throws Exception {

        // given
        Mockito.when(paymentService.getOnePayment(Mockito.any(Long.class)))
                .thenReturn(getPaymentCardResponse());

        // when
        ResultActions resultActions = mockMvc.perform(get("/payments/1"));

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
        Mockito.when(paymentService.getTotalPrice()).thenReturn(getTotalPayment());

        // when
        ResultActions resultActions = mockMvc.perform(get("/payments/total"));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(30000));
    }

    @Test
    void it_should_get_one_user_payments() throws Exception {

        // given
        Mockito.when(paymentService.getOneUserPayment(Mockito.any(Long.class))).thenReturn(getOneUserTotalPayment());

        // when
        ResultActions resultActions = mockMvc.perform(get("/payments/users/1"));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(139.5));
    }

    private BigDecimal getOneUserTotalPayment() {
        return new BigDecimal("139.5");
    }

    private BigDecimal getTotalPayment() {
        return new BigDecimal("30000");
    }

    private PaymentTransferSendRequest getPaymentTransferSendRequest() {
        return new PaymentTransferSendRequest("TR 000212564899147532", "TR 090002562311458900", "MEDIUM", 2L, "test user");
    }

    private PaymentResponse getPaymentTransferResponse() {
        return new PaymentResponse(2L, "test message", 2L, PaymentType.TRANSFER, PaymentStatus.ACCEPTED);
    }

    private PaymentCarSendRequest getPaymentCarSendRequest() {
        return new PaymentCarSendRequest("4441113332", LocalDate.now(Clock.systemDefaultZone()), "131", 2L, "test user");
    }

    private PaymentResponse getPaymentCardResponse() {
        return new PaymentResponse(1L, "test message", 2L, PaymentType.CARD, PaymentStatus.ACCEPTED);
    }

    private List<PaymentResponse> getAllPaymentResponses() {
        return List.of(getPaymentCardResponse(), getPaymentTransferResponse());
    }

}