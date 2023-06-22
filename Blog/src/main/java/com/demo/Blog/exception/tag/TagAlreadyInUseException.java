package com.demo.Blog.exception.tag;

public class TagAlreadyInUseException extends RuntimeException {
    public TagAlreadyInUseException(String message) {
        super(message);
    }
}
