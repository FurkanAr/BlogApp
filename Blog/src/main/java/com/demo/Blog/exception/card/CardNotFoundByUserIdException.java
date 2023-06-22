package com.demo.Blog.exception.card;

public class CardNotFoundByUserIdException extends RuntimeException {
    public CardNotFoundByUserIdException(String message) {
        super(message);
    }
}
