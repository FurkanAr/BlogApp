package com.demo.Blog.exception.user;

public class PasswordNotCorrectException extends RuntimeException {

    public PasswordNotCorrectException(String message){
        super(message);
    }
}
