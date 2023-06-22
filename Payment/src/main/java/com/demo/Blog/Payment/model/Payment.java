package com.demo.Blog.Payment.model;

import com.demo.Blog.Payment.model.enums.PaymentStatus;
import com.demo.Blog.Payment.model.enums.PaymentType;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;
    @Column(name = "create_date")
    private LocalDateTime createdDate;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "user_name")
    private String userName;
    @Column(name = "payment_type")
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;
    @Column(name = "price")
    private Double price;
    @Column(name = "user_account_number")
    private String userAccountNumber;
    @Column(name = "user_card_number")
    private String userCardNumber;

    @Column(name= "status")
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Column(name = "message")
    private String message;

    public Payment() {
    }

    public Payment(Long id, LocalDateTime createdDate, Long userId, String userName, PaymentType paymentType, Double price, String userAccountNumber, String userCardNumber, PaymentStatus status, String message) {
        this.id = id;
        this.createdDate = createdDate;
        this.userId = userId;
        this.userName = userName;
        this.paymentType = paymentType;
        this.price = price;
        this.userAccountNumber = userAccountNumber;
        this.userCardNumber = userCardNumber;
        this.status = status;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getUserAccountNumber() {
        return userAccountNumber;
    }

    public void setUserAccountNumber(String userAccountNumber) {
        this.userAccountNumber = userAccountNumber;
    }

    public String getUserCardNumber() {
        return userCardNumber;
    }

    public void setUserCardNumber(String userCardNumber) {
        this.userCardNumber = userCardNumber;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "id=" + id +
                ", createdDate=" + createdDate +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                ", paymentType=" + paymentType +
                ", price=" + price +
                ", status=" + status +
                ", message='" + message + '\'' +
                '}';
    }
}
