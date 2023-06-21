package com.demo.Blog.converter;

import com.demo.Blog.client.request.PaymentCardGetRequest;
import com.demo.Blog.model.CardInfo;
import com.demo.Blog.model.User;
import org.springframework.stereotype.Component;

@Component
public class CardConverter {

    public CardInfo convert(PaymentCardGetRequest paymentCardGetRequest, User user) {
        CardInfo cardInfo = new CardInfo();
        cardInfo.setCardNo(paymentCardGetRequest.getCardNo());
        cardInfo.setCvcNo(paymentCardGetRequest.getCvcNo());
        cardInfo.setExpireDate(paymentCardGetRequest.getExpireDate());
        cardInfo.setUser(user);
        return cardInfo;
    }

}
