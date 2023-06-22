package com.demo.Blog.exception.membership;

public class MembershipNotFoundByUserIdException extends RuntimeException {
    public MembershipNotFoundByUserIdException(String message) {
        super(message);
    }
}
