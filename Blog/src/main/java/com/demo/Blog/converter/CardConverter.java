package com.demo.Blog.converter;

import com.demo.Blog.client.request.PaymentCardGetRequest;
import com.demo.Blog.model.CardInfo;
import com.demo.Blog.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CardConverter {

    Logger logger = LoggerFactory.getLogger(getClass());

    public CardInfo convert(PaymentCardGetRequest paymentCardGetRequest, User user) {
        logger.info("convert to CardInfo method started");
        CardInfo cardInfo = new CardInfo();
        cardInfo.setCardNo(paymentCardGetRequest.getCardNo());
        cardInfo.setCvcNo(paymentCardGetRequest.getCvcNo());
        cardInfo.setExpireDate(paymentCardGetRequest.getExpireDate());
        cardInfo.setUser(user);
        logger.info("convert to CardInfo method successfully worked");
        return cardInfo;
    }

}
