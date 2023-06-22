package com.demo.Blog.exception.membership;

public class UserHasMembershipException extends RuntimeException {
    public UserHasMembershipException(String message) {
        super(message);
    }
}
