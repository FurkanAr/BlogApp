package com.demo.Blog.client.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class PaymentCardGetRequest {
    @NotEmpty(message = "Please enter your cardNo")
    private String cardNo;
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @NotNull(message = "Please enter your card expire date")
    private LocalDate expireDate;
    @NotEmpty(message = "Please enter your card cvcNo")
    private String cvcNo;
    private Long userId;

    public PaymentCardGetRequest() {
    }

    public PaymentCardGetRequest(String cardNo, LocalDate expireDate, String cvcNo, Long userId) {
        this.cardNo = cardNo;
        this.expireDate = expireDate;
        this.cvcNo = cvcNo;
        this.userId = userId;
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

    @Override
    public String toString() {
        return "PaymentCardGetRequest{" +
                "cardNo='" + cardNo + '\'' +
                ", expireDate=" + expireDate +
                ", cvcNo='" + cvcNo + '\'' +
                ", userId=" + userId +
                '}';
    }
}
