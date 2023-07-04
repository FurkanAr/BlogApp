package com.demo.Blog.client.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class PaymentTransferGetRequest {

    @NotEmpty(message = "Please enter your account number")
    private String accountNumberFrom;
    @NotEmpty(message = "Please enter customer account number")
    private String accountNumberTo;
    @NotEmpty(message = "Please enter firm name")
    private String firmName;
    @NotNull(message = "Please enter your userId")
    private Long userId;


    public PaymentTransferGetRequest() {
    }

    public PaymentTransferGetRequest(String accountNumberFrom, String accountNumberTo, String firmName, Long userId) {
        this.accountNumberFrom = accountNumberFrom;
        this.accountNumberTo = accountNumberTo;
        this.firmName = firmName;
        this.userId = userId;
    }

    public String getAccountNumberFrom() {
        return accountNumberFrom;
    }

    public void setAccountNumberFrom(String accountNumberFrom) {
        this.accountNumberFrom = accountNumberFrom;
    }

    public String getAccountNumberTo() {
        return accountNumberTo;
    }

    public void setAccountNumberTo(String accountNumberTo) {
        this.accountNumberTo = accountNumberTo;
    }

    public String getFirmName() {
        return firmName;
    }

    public void setFirmName(String firmName) {
        this.firmName = firmName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }


    @Override
    public String toString() {
        return "PaymentTransferRequest{" +
                "accountNumberFrom='" + accountNumberFrom + '\'' +
                ", accountNumberTo='" + accountNumberTo + '\'' +
                ", firmName='" + firmName + '\'' +
                ", userId=" + userId +
                '}';
    }
}