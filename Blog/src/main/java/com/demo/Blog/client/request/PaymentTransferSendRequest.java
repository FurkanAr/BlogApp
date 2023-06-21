package com.demo.Blog.client.request;

public class PaymentTransferSendRequest {

    private String accountNumberFrom;
    private String accountNumberTo;
    private String firmName;
    private Long userId;
    private String userName;

    public PaymentTransferSendRequest() {
    }

    public PaymentTransferSendRequest(String accountNumberFrom, String accountNumberTo, String firmName, Long userId, String userName) {
        this.accountNumberFrom = accountNumberFrom;
        this.accountNumberTo = accountNumberTo;
        this.firmName = firmName;
        this.userId = userId;
        this.userName = userName;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    @Override
    public String toString() {
        return "PaymentTransferRequest{" +
                "accountNumberFrom='" + accountNumberFrom + '\'' +
                ", accountNumberTo='" + accountNumberTo + '\'' +
                ", firmName='" + firmName + '\'' +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                '}';
    }
}