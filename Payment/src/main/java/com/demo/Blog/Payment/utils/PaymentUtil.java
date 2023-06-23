package com.demo.Blog.Payment.utils;

import com.demo.Blog.Payment.model.Payment;
import com.demo.Blog.Payment.model.enums.PaymentStatus;
import com.demo.Blog.Payment.request.PaymentCarSendRequest;
import com.demo.Blog.Payment.request.PaymentTransferSendRequest;

import java.time.LocalDate;

public class PaymentUtil {

    private static final Integer CARD_NO_LENGTH = 10;
    private static final Integer CVC_NO_LENGTH = 3;
    private static final String ACCOUNT_STARTS_WITH = "TR ";
    private static final String ACCOUNT_NUMBER = "TR 090002562311458900";
    private static final String FIRM_NAME = "MEDIUM";

    private static final String MESSAGE = "Payment successful, Enjoy!!";


    private PaymentUtil() {
    }

    public static boolean cardPaymentControl(PaymentCarSendRequest paymentCarSendRequest){
        return (paymentCarSendRequest.getCardNo().length() == CARD_NO_LENGTH &&
                paymentCarSendRequest.getCvcNo().length() == CVC_NO_LENGTH &&
                paymentCarSendRequest.getExpireDate().isAfter(LocalDate.now()));
    }

    public static boolean transferPaymentControl(PaymentTransferSendRequest paymentTransferSendRequest) {
        return (paymentTransferSendRequest.getAccountNumberFrom().startsWith(ACCOUNT_STARTS_WITH) &&
                paymentTransferSendRequest.getAccountNumberTo().equals(ACCOUNT_NUMBER) &&
                paymentTransferSendRequest.getFirmName().equals(FIRM_NAME));
    }

    public static void changePaymentStatusAndMessage(Payment payment) {
        payment.setMessage(MESSAGE);
        payment.setStatus(PaymentStatus.ACCEPTED);
    }
}
