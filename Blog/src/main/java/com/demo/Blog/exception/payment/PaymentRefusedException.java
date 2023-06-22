package com.demo.Blog.exception.payment;

public class PaymentRefusedException extends RuntimeException {
    public PaymentRefusedException(String message) {
        super(message);
    }
}
