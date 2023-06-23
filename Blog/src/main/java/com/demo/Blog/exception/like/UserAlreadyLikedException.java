package com.demo.Blog.exception.like;

public class UserAlreadyLikedException extends RuntimeException {
    public UserAlreadyLikedException(String message) {
        super(message);
    }
}
