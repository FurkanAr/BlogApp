package com.demo.Blog.service;

import com.demo.Blog.client.request.PaymentCardGetRequest;
import com.demo.Blog.converter.CardConverter;
import com.demo.Blog.exception.card.CardNotFoundByUserIdException;
import com.demo.Blog.exception.card.CardNotFoundException;
import com.demo.Blog.exception.messages.Messages;
import com.demo.Blog.model.CardInfo;
import com.demo.Blog.model.User;
import com.demo.Blog.repository.CardInfoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CardService {

    private final CardInfoRepository cardInfoRepository;
    private final CardConverter cardConverter;

    Logger logger = LoggerFactory.getLogger(getClass());

    public CardService(CardInfoRepository cardInfoRepository, CardConverter cardConverter) {
        this.cardInfoRepository = cardInfoRepository;
        this.cardConverter = cardConverter;
    }
    @Transactional
    protected void saveCard(PaymentCardGetRequest paymentCardGetRequest, User user){
        logger.debug("saveCard method started");
        CardInfo card = cardInfoRepository.save(cardConverter.convert(paymentCardGetRequest, user));
        logger.info("Card created: {}", card.getId());
        logger.info("saveCard method successfully worked");
    }

    protected CardInfo getCardById(Long id){
        logger.debug("getCardById method started");
        CardInfo cardInfo = cardInfoRepository.findById(id).orElseThrow(() ->
                new CardNotFoundException(Messages.Card.NOT_EXISTS_BY_ID + id));
        logger.info("Requested card: {}", id);
        logger.info("getCardById method successfully worked");
        return cardInfo;
    }

    protected CardInfo getCardByUserId(Long userId){
        logger.debug("getCardByUserId method started");
        CardInfo cardInfo = cardInfoRepository.findByUserId(userId).orElseThrow(() ->
                new CardNotFoundByUserIdException(Messages.Card.NOT_EXIST_BY_USER_ID + userId));
        logger.info("Requested card by user: {}", userId);
        logger.info("getCardByUserId method successfully worked");
        return cardInfo;
    }

}
