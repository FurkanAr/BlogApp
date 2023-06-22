package com.demo.Blog.Payment.response;


import com.demo.Blog.Payment.model.enums.PaymentStatus;
import com.demo.Blog.Payment.model.enums.PaymentType;

public class PaymentResponse {

    private Long id;
    private String message;
    private Long userId;
    private PaymentType paymentType;
    private PaymentStatus status;


    public PaymentResponse() {
    }

    public PaymentResponse(Long id, String message, Long userId, PaymentType paymentType, PaymentStatus status) {
        this.id = id;
        this.message = message;
        this.userId = userId;
        this.paymentType = paymentType;
        this.status = status;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "PaymentResponse{" +
                "id=" + id +
                ", message='" + message + '\'' +
                ", userId=" + userId +
                ", paymentType=" + paymentType +
                ", status=" + status +
                '}';
    }
}
