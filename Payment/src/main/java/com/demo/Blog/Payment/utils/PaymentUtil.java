package com.demo.Blog.Payment.utils;

import com.demo.Blog.Payment.constants.Constant;
import com.demo.Blog.Payment.model.Payment;
import com.demo.Blog.Payment.model.enums.PaymentStatus;
import com.demo.Blog.Payment.request.PaymentCarSendRequest;
import com.demo.Blog.Payment.request.PaymentTransferSendRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

public class PaymentUtil {

    static Logger logger = LoggerFactory.getLogger(PaymentUtil.class);

    private PaymentUtil() {
    }

    public static boolean cardPaymentControl(PaymentCarSendRequest paymentCarSendRequest){
        logger.info("cardPaymentControl method started");
        boolean isCorrect = (paymentCarSendRequest.getCardNo().length() == Constant.Payment.CARD_NO_LENGTH &&
                paymentCarSendRequest.getCvcNo().length() == Constant.Payment.CVC_NO_LENGTH &&
                paymentCarSendRequest.getExpireDate().isAfter(LocalDate.now()));
        logger.info("PaymentCarSendRequest information is correct: {}" , isCorrect);
        logger.info("cardPaymentControl method successfully worked");
        return isCorrect;
    }

    public static boolean transferPaymentControl(PaymentTransferSendRequest paymentTransferSendRequest) {
        logger.info("transferPaymentControl method started");
        boolean isCorrect = (paymentTransferSendRequest.getAccountNumberFrom().startsWith(Constant.Payment.ACCOUNT_STARTS_WITH) &&
                paymentTransferSendRequest.getAccountNumberTo().equals(Constant.Payment.ACCOUNT_NUMBER) &&
                paymentTransferSendRequest.getFirmName().equals(Constant.Payment.FIRM_NAME));
        logger.info("PaymentTransferSendRequest information is correct: {}" , isCorrect);
        logger.info("transferPaymentControl method successfully worked");
        return isCorrect;
    }

    public static void changePaymentStatusAndMessage(Payment payment) {
        logger.info("changePaymentStatusAndMessage method started");
        payment.setMessage(Constant.Payment.ACCEPTED_MESSAGE);
        payment.setStatus(PaymentStatus.ACCEPTED);
        logger.info("Payment message: {}, status: {} updated", Constant.Payment.ACCEPTED_MESSAGE, PaymentStatus.ACCEPTED);
        logger.info("changePaymentStatusAndMessage method successfully worked");
    }
}
