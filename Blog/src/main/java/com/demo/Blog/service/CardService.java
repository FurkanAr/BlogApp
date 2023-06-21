package com.demo.Blog.service;

import com.demo.Blog.client.request.PaymentCardGetRequest;
import com.demo.Blog.converter.CardConverter;
import com.demo.Blog.model.CardInfo;
import com.demo.Blog.model.User;
import com.demo.Blog.repository.CardInfoRepository;
import org.springframework.stereotype.Service;

@Service
public class CardService {

    private final CardInfoRepository cardInfoRepository;
    private final CardConverter cardConverter;


    public CardService(CardInfoRepository cardInfoRepository, CardConverter cardConverter) {
        this.cardInfoRepository = cardInfoRepository;
        this.cardConverter = cardConverter;
    }

    protected CardInfo saveCard(PaymentCardGetRequest paymentCardGetRequest, User user){
        CardInfo card = cardConverter.convert(paymentCardGetRequest, user);
        cardInfoRepository.save(card);
        return card;
    }

    protected CardInfo getCardById(Long id){
        CardInfo cardInfo = cardInfoRepository.findById(id).orElseThrow(() -> new RuntimeException("card cannot found! id: " + id));
        return cardInfo;
    }

    protected CardInfo getCardByUserId(Long userId){
        CardInfo cardInfo = cardInfoRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("card cannot found! userId: " + userId));
        return cardInfo;
    }

}
