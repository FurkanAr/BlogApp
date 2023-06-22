package com.demo.Blog.exception.membership;

public class MembershipIsExpiredException extends RuntimeException {
    public MembershipIsExpiredException(String message) {
        super(message);
    }
}
