package com.demo.Blog.Payment.converter;

import com.demo.Blog.Payment.model.Payment;
import com.demo.Blog.Payment.model.enums.PaymentStatus;
import com.demo.Blog.Payment.model.enums.PaymentType;
import com.demo.Blog.Payment.request.PaymentCarSendRequest;
import com.demo.Blog.Payment.request.PaymentTransferSendRequest;
import com.demo.Blog.Payment.response.PaymentResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class PaymentConverter {

    private static final String MESSAGE = "Payment failed, try again!";
    private static final String NO_USAGE_CARD_TRANSFER = "No information";

    private static Double PRICE = 39.99;

    public Payment convert(PaymentTransferSendRequest paymentTransferSendRequest){
        Payment payment = new Payment();
        payment.setPaymentType(PaymentType.TRANSFER);
        payment.setCreatedDate(LocalDateTime.now());
        payment.setPrice(PRICE);
        payment.setUserId(paymentTransferSendRequest.getUserId());
        payment.setUserName(paymentTransferSendRequest.getUserName());
        payment.setUserAccountNumber(paymentTransferSendRequest.getAccountNumberFrom());
        payment.setUserCardNumber(NO_USAGE_CARD_TRANSFER);
        payment.setStatus(PaymentStatus.REFUSED);
        payment.setMessage(MESSAGE);
        return payment;
    }

    public Payment convert(PaymentCarSendRequest paymentCarSendRequest){
        Payment payment = new Payment();
        payment.setPaymentType(PaymentType.CARD);
        payment.setCreatedDate(LocalDateTime.now());
        payment.setPrice(PRICE);
        payment.setUserId(paymentCarSendRequest.getUserId());
        payment.setUserName(paymentCarSendRequest.getUserName());
        payment.setUserAccountNumber(NO_USAGE_CARD_TRANSFER);
        payment.setUserCardNumber(paymentCarSendRequest.getCardNo());
        payment.setStatus(PaymentStatus.REFUSED);
        payment.setMessage(MESSAGE);
        return payment;
    }

    public PaymentResponse convert(Payment payment){
        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setId(payment.getId());
        paymentResponse.setMessage(payment.getMessage());
        paymentResponse.setPaymentType(payment.getPaymentType());
        paymentResponse.setUserId(payment.getUserId());
        paymentResponse.setStatus(payment.getStatus());
        return paymentResponse;
    }

    public List<PaymentResponse> convert(List<Payment> paymentList){
        List<PaymentResponse> paymentResponses = new ArrayList<>();
        paymentList.stream().forEach(payment -> paymentResponses.add(convert(payment)));
        return paymentResponses;
    }

}
