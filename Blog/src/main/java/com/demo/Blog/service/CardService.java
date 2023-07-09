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

    private static final String CARD_SAVED = "Card successfully saved!!";
    private final CardInfoRepository cardInfoRepository;
    private final CardConverter cardConverter;

    Logger logger = LoggerFactory.getLogger(getClass());

    public CardService(CardInfoRepository cardInfoRepository, CardConverter cardConverter) {
        this.cardInfoRepository = cardInfoRepository;
        this.cardConverter = cardConverter;
    }

    @Transactional
    protected String saveCard(PaymentCardGetRequest paymentCardGetRequest, User user) {
        logger.info("saveCard method started");
        CardInfo card = cardInfoRepository.save(cardConverter.convert(paymentCardGetRequest, user));
        logger.info("Card created: {}", card.getId());
        logger.info("saveCard method successfully worked");
        return CARD_SAVED;
    }

    protected CardInfo getCardById(Long id) {
        logger.info("getCardById method started");
        CardInfo cardInfo = cardInfoRepository.findById(id).orElseThrow(() ->
                new CardNotFoundException(Messages.Card.NOT_EXISTS_BY_ID + id));
        logger.info("Found card by id: {} ", id);
        logger.info("getCardById method successfully worked");
        return cardInfo;
    }

    protected CardInfo getCardByUserId(Long userId) {
        logger.info("getCardByUserId method started");
        CardInfo cardInfo = cardInfoRepository.findByUserId(userId).orElseThrow(() ->
                new CardNotFoundByUserIdException(Messages.Card.NOT_EXIST_BY_USER_ID + userId));
        logger.info("Found card: {}, by user: {} ", cardInfo.getId(), userId);
        logger.info("getCardByUserId method successfully worked");
        return cardInfo;
    }

}
