package com.demo.Blog.client.converter;

import com.demo.Blog.client.request.PaymentCardGetRequest;
import com.demo.Blog.client.request.PaymentCardSendRequest;
import com.demo.Blog.client.request.PaymentTransferGetRequest;
import com.demo.Blog.client.request.PaymentTransferSendRequest;
import com.demo.Blog.model.CardInfo;
import com.demo.Blog.model.Membership;
import com.demo.Blog.model.User;
import org.springframework.stereotype.Component;

@Component
public class PaymentConverter {

    public PaymentCardSendRequest convert(User user, PaymentCardGetRequest paymentCardGetRequest){
        PaymentCardSendRequest paymentCardSendRequest = new PaymentCardSendRequest();
        paymentCardSendRequest.setUserName(user.getUserName());
        paymentCardSendRequest.setUserId(user.getId());
        paymentCardSendRequest.setCardNo(paymentCardGetRequest.getCardNo());
        paymentCardSendRequest.setCvcNo(paymentCardGetRequest.getCvcNo());
        paymentCardSendRequest.setExpireDate(paymentCardGetRequest.getExpireDate());
        return paymentCardSendRequest;
    }


    public PaymentTransferSendRequest convert(User user, PaymentTransferGetRequest paymentTransferGetRequest){
        PaymentTransferSendRequest paymentTransferSendRequest = new PaymentTransferSendRequest();
        paymentTransferSendRequest.setUserName(user.getUserName());
        paymentTransferSendRequest.setUserId(user.getId());
        paymentTransferSendRequest.setAccountNumberFrom(paymentTransferGetRequest.getAccountNumberFrom());
        paymentTransferSendRequest.setAccountNumberTo(paymentTransferGetRequest.getAccountNumberTo());
        paymentTransferSendRequest.setFirmName(paymentTransferGetRequest.getFirmName());
        return paymentTransferSendRequest;
    }


    public PaymentCardSendRequest getCardInfoForPayment(Membership membership, CardInfo cardInfo) {
        PaymentCardSendRequest paymentCardSendRequest = new PaymentCardSendRequest();
        paymentCardSendRequest.setUserName(membership.getUser().getUserName());
        paymentCardSendRequest.setUserId(membership.getUser().getId());
        paymentCardSendRequest.setCardNo(cardInfo.getCardNo());
        paymentCardSendRequest.setCvcNo(cardInfo.getCvcNo());
        paymentCardSendRequest.setExpireDate(cardInfo.getExpireDate());
        return paymentCardSendRequest;
    }


}
