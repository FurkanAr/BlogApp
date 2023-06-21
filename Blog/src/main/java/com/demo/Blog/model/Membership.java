package com.demo.Blog.model;

import com.demo.Blog.model.enums.MembershipStatus;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "membership")
public class Membership {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "start_date")
    private LocalDate startDate;
    @Column(name = "expire_date")
    private LocalDate expireDate;

    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.PERSIST,
            CascadeType.REFRESH,})
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private MembershipStatus status = MembershipStatus.ACTIVE;

    public Membership() {
    }

    public Membership(LocalDate startDate, LocalDate expireDate, User user, MembershipStatus status) {
        this.startDate = startDate;
        this.expireDate = expireDate;
        this.user = user;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public MembershipStatus getStatus() {
        return status;
    }

    public void setStatus(MembershipStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Membership{" +
                "id=" + id +
                ", startDate=" + startDate +
                ", expireDate=" + expireDate +
                ", user=" + user +
                ", status=" + status +
                '}';
    }
}
