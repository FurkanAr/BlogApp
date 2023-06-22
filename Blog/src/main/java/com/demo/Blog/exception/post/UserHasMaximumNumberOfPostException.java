package com.demo.Blog.exception.post;

public class UserHasMaximumNumberOfPostException extends RuntimeException {
    public UserHasMaximumNumberOfPostException(String message) {
        super(message);
    }
}
