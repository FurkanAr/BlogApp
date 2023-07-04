package com.demo.Blog.Payment.utils;

import com.demo.Blog.Payment.model.Payment;
import com.demo.Blog.Payment.model.enums.PaymentStatus;
import com.demo.Blog.Payment.request.PaymentCarSendRequest;
import com.demo.Blog.Payment.request.PaymentTransferSendRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

public class PaymentUtil {

    private static final Integer CARD_NO_LENGTH = 10;
    private static final Integer CVC_NO_LENGTH = 3;
    private static final String ACCOUNT_STARTS_WITH = "TR ";
    private static final String ACCOUNT_NUMBER = "TR 090002562311458900";
    private static final String FIRM_NAME = "MEDIUM";
    private static final String MESSAGE = "Payment successful, Enjoy!!";

    static Logger logger = LoggerFactory.getLogger(PaymentUtil.class);

    private PaymentUtil() {
    }

    public static boolean cardPaymentControl(PaymentCarSendRequest paymentCarSendRequest){
        logger.info("cardPaymentControl method started");
        boolean isCorrect = (paymentCarSendRequest.getCardNo().length() == CARD_NO_LENGTH &&
                paymentCarSendRequest.getCvcNo().length() == CVC_NO_LENGTH &&
                paymentCarSendRequest.getExpireDate().isAfter(LocalDate.now()));
        logger.info("PaymentCarSendRequest information is correct: {}" , isCorrect);
        logger.info("cardPaymentControl method successfully worked");
        return isCorrect;
    }

    public static boolean transferPaymentControl(PaymentTransferSendRequest paymentTransferSendRequest) {
        logger.info("transferPaymentControl method started");
        boolean isCorrect = (paymentTransferSendRequest.getAccountNumberFrom().startsWith(ACCOUNT_STARTS_WITH) &&
                paymentTransferSendRequest.getAccountNumberTo().equals(ACCOUNT_NUMBER) &&
                paymentTransferSendRequest.getFirmName().equals(FIRM_NAME));
        logger.info("PaymentTransferSendRequest information is correct: {}" , isCorrect);
        logger.info("transferPaymentControl method successfully worked");
        return isCorrect;
    }

    public static void changePaymentStatusAndMessage(Payment payment) {
        logger.info("changePaymentStatusAndMessage method started");
        payment.setMessage(MESSAGE);
        payment.setStatus(PaymentStatus.ACCEPTED);
        logger.info("Payment message: {}, status: {} updated", MESSAGE, PaymentStatus.ACCEPTED);
        logger.info("changePaymentStatusAndMessage method successfully worked");
    }
}
