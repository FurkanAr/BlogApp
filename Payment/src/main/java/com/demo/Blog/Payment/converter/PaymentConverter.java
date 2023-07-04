package com.demo.Blog.Payment.converter;

import com.demo.Blog.Payment.model.Payment;
import com.demo.Blog.Payment.model.enums.PaymentStatus;
import com.demo.Blog.Payment.model.enums.PaymentType;
import com.demo.Blog.Payment.request.PaymentCarSendRequest;
import com.demo.Blog.Payment.request.PaymentTransferSendRequest;
import com.demo.Blog.Payment.response.PaymentResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class PaymentConverter {

    private static final String MESSAGE = "Payment failed, try again!";
    private static final String NO_USAGE_CARD_TRANSFER = "No information";
    private static final Double PRICE = 39.99;

    Logger logger = LoggerFactory.getLogger(getClass());

    public Payment convert(PaymentTransferSendRequest paymentTransferSendRequest){
        logger.info("convert to Payment method started");
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
        logger.info("convert to Payment method successfully worked");
        return payment;
    }

    public Payment convert(PaymentCarSendRequest paymentCarSendRequest){
        logger.info("convert to Payment method started");
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
        logger.info("convert to Payment method successfully worked");
        return payment;
    }

    public PaymentResponse convert(Payment payment){
        logger.info("convert to Response method started");
        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setId(payment.getId());
        paymentResponse.setMessage(payment.getMessage());
        paymentResponse.setPaymentType(payment.getPaymentType());
        paymentResponse.setUserId(payment.getUserId());
        paymentResponse.setStatus(payment.getStatus());
        logger.info("convert to Response method successfully worked");
        return paymentResponse;
    }

    public List<PaymentResponse> convert(List<Payment> paymentList){
        logger.info("convert paymentList to paymentResponses method started");
        List<PaymentResponse> paymentResponses = new ArrayList<>();
        paymentList.forEach(payment -> paymentResponses.add(convert(payment)));
        logger.info("convert paymentList to paymentResponses method successfully worked");
        return paymentResponses;
    }

}
