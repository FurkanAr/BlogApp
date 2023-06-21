package com.demo.Blog.response;

import com.demo.Blog.model.enums.MembershipStatus;

import java.time.LocalDate;

public class MembershipResponse {
    private Long id;
    private LocalDate startDate;
    private LocalDate expireDate;
    private Long userId;
    private MembershipStatus status;

    public MembershipResponse() {
    }

    public MembershipResponse(Long id, LocalDate startDate, LocalDate expireDate, Long userId, MembershipStatus status) {
        this.id = id;
        this.startDate = startDate;
        this.expireDate = expireDate;
        this.userId = userId;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(LocalDate expireDate) {
        this.expireDate = expireDate;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public MembershipStatus getStatus() {
        return status;
    }

    public void setStatus(MembershipStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "MembershipResponse{" +
                "id=" + id +
                ", startDate=" + startDate +
                ", expireDate=" + expireDate +
                ", userId=" + userId +
                ", status=" + status +
                '}';
    }
}
