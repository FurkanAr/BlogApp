package com.demo.Blog.Payment.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import java.time.LocalDate;

public class PaymentCarSendRequest {

    private String cardNo;
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate expireDate;
    private String cvcNo;
    private Long userId;
    private String userName;

    public PaymentCarSendRequest() {
    }

    public PaymentCarSendRequest(String cardNo, LocalDate expireDate, String cvcNo, Long userId, String userName) {
        this.cardNo = cardNo;
        this.expireDate = expireDate;
        this.cvcNo = cvcNo;
        this.userId = userId;
        this.userName = userName;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public LocalDate getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(LocalDate expireDate) {
        this.expireDate = expireDate;
    }

    public String getCvcNo() {
        return cvcNo;
    }

    public void setCvcNo(String cvcNo) {
        this.cvcNo = cvcNo;
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


    @Override
    public String toString() {
        return "PaymentCarSendRequest{" +
                "cardNo='" + cardNo + '\'' +
                ", expireDate=" + expireDate +
                ", cvcNo=" + cvcNo +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                '}';
    }
}
