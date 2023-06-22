package com.demo.Blog.exception.tag;

public class TagNotFoundGivenTagNameException extends RuntimeException {
    public TagNotFoundGivenTagNameException(String message) {
        super(message);
    }
}
