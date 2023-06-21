package com.demo.Blog.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "card_info")
public class CardInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "card_no")
    private String cardNo;
    @Column(name = "expire_date")
    private LocalDate expireDate;
    @Column(name = "cvc_no")
    private String cvcNo;
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.PERSIST,
            CascadeType.REFRESH,})
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public CardInfo() {
    }

    public CardInfo(String cardNo, LocalDate expireDate, String cvcNo, User user) {
        this.cardNo = cardNo;
        this.expireDate = expireDate;
        this.cvcNo = cvcNo;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "CardInfo{" +
                "id=" + id +
                ", cardNo='" + cardNo + '\'' +
                ", expireDate=" + expireDate +
                ", cvcNo='" + cvcNo + '\'' +
                ", user=" + user +
                '}';
    }
}
