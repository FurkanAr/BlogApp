package com.demo.Blog.client.request;

import javax.validation.constraints.NotEmpty;

public class RenewMembershipCardRequest {
    @NotEmpty(message = "Please enter your card id")
    private Long cardId;
    @NotEmpty(message = "Please enter your userId")
    private Long userId;

    public RenewMembershipCardRequest() {
    }

    public RenewMembershipCardRequest(Long cardId, Long userId) {
        this.cardId = cardId;
        this.userId = userId;
    }

    public Long getCardId() {
        return cardId;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "RenewMembershipCardRequest{" +
                "cardId=" + cardId +
                ", userId=" + userId +
                '}';
    }
}
