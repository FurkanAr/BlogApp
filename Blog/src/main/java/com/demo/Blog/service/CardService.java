package com.demo.Blog.service;

import com.demo.Blog.client.request.PaymentCardGetRequest;
import com.demo.Blog.converter.CardConverter;
import com.demo.Blog.exception.card.CardNotFoundByUserIdException;
import com.demo.Blog.exception.card.CardNotFoundException;
import com.demo.Blog.exception.messages.Messages;
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
        CardInfo card = cardInfoRepository.save(cardConverter.convert(paymentCardGetRequest, user));
        return card;
    }

    protected CardInfo getCardById(Long id){
        CardInfo cardInfo = cardInfoRepository.findById(id).orElseThrow(() ->
                new CardNotFoundException(Messages.Card.NOT_EXISTS_BY_ID + id));
        return cardInfo;
    }

    protected CardInfo getCardByUserId(Long userId){
        CardInfo cardInfo = cardInfoRepository.findByUserId(userId).orElseThrow(() ->
                new CardNotFoundByUserIdException(Messages.Card.NOT_EXIST_BY_USER_ID + userId));
        return cardInfo;
    }

}
