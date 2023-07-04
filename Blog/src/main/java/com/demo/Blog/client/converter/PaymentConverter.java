package com.demo.Blog.client.converter;

import com.demo.Blog.client.request.PaymentCardGetRequest;
import com.demo.Blog.client.request.PaymentCardSendRequest;
import com.demo.Blog.client.request.PaymentTransferGetRequest;
import com.demo.Blog.client.request.PaymentTransferSendRequest;
import com.demo.Blog.model.CardInfo;
import com.demo.Blog.model.Membership;
import com.demo.Blog.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class PaymentConverter {

    Logger logger = LoggerFactory.getLogger(getClass());

    public PaymentCardSendRequest convert(User user, PaymentCardGetRequest paymentCardGetRequest){
        logger.info("convert to PaymentCardSendRequest method started");

        PaymentCardSendRequest paymentCardSendRequest = new PaymentCardSendRequest();
        paymentCardSendRequest.setUserName(user.getUserName());
        paymentCardSendRequest.setUserId(user.getId());
        paymentCardSendRequest.setCardNo(paymentCardGetRequest.getCardNo());
        paymentCardSendRequest.setCvcNo(paymentCardGetRequest.getCvcNo());
        paymentCardSendRequest.setExpireDate(paymentCardGetRequest.getExpireDate());

        logger.info("convert to PaymentCardSendRequest method successfully worked");
        return paymentCardSendRequest;
    }


    public PaymentTransferSendRequest convert(User user, PaymentTransferGetRequest paymentTransferGetRequest){
        logger.info("convert to PaymentTransferSendRequest method started");

        PaymentTransferSendRequest paymentTransferSendRequest = new PaymentTransferSendRequest();
        paymentTransferSendRequest.setUserName(user.getUserName());
        paymentTransferSendRequest.setUserId(user.getId());
        paymentTransferSendRequest.setAccountNumberFrom(paymentTransferGetRequest.getAccountNumberFrom());
        paymentTransferSendRequest.setAccountNumberTo(paymentTransferGetRequest.getAccountNumberTo());
        paymentTransferSendRequest.setFirmName(paymentTransferGetRequest.getFirmName());

        logger.info("convert to PaymentTransferSendRequest method successfully worked");
        return paymentTransferSendRequest;
    }


    public PaymentCardSendRequest getCardInfoForPayment(Membership membership, CardInfo cardInfo) {
        logger.info("getCardInfoForPayment to PaymentCardSendRequest method started");

        PaymentCardSendRequest paymentCardSendRequest = new PaymentCardSendRequest();
        paymentCardSendRequest.setUserName(membership.getUser().getUserName());
        paymentCardSendRequest.setUserId(membership.getUser().getId());
        paymentCardSendRequest.setCardNo(cardInfo.getCardNo());
        paymentCardSendRequest.setCvcNo(cardInfo.getCvcNo());
        paymentCardSendRequest.setExpireDate(cardInfo.getExpireDate());

        logger.info("getCardInfoForPayment to PaymentCardSendRequest method successfully worked");
        return paymentCardSendRequest;
    }


}
