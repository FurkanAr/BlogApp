package com.demo.Blog.service;

import com.demo.Blog.client.PaymentServiceClient;
import com.demo.Blog.client.response.PaymentResponse;
import com.demo.Blog.response.MembershipResponse;
import com.demo.Blog.response.PostResponse;
import com.demo.Blog.response.UserResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class DashboardService {

    private final UserService userService;
    private final PostService postService;
    private final MembershipService membershipService;

    private final PaymentServiceClient paymentServiceClient;

    public DashboardService(UserService userService, PostService postService, MembershipService membershipService, PaymentServiceClient paymentServiceClient) {
        this.userService = userService;
        this.postService = postService;
        this.membershipService = membershipService;
        this.paymentServiceClient = paymentServiceClient;
    }
    public List<UserResponse> getAllUsers() {
        return userService.findAll();
    }
    public UserResponse getUserById(Long userId) {
        return userService.findById(userId);
    }

    public Long getTotalNumberOfUser() {
        return userService.findNumberOfUsers();
    }

    public List<PostResponse> getAllPosts(Optional<Long> userId) {
        return postService.getAllPosts(userId);
    }

    public List<MembershipResponse> getAllMemberships() {
        return membershipService.getAllMemberships();
    }

    public MembershipResponse getOneMembership(Long membershipId) {
        return membershipService.getOneMembership(membershipId);
    }

    public Long getTotalNumberOfMemberships() {
        return membershipService.getTotalNumberOfMemberships();
    }

    public List<PaymentResponse> getAllPayments() {
        return paymentServiceClient.getAllPayments();
    }

    public PaymentResponse getOnePayment(Long paymentId) {
        return paymentServiceClient.getOnePayment(paymentId);
    }

    public BigDecimal getTotalPaymentPrice() {
        return paymentServiceClient.getTotalPrice();
    }

    public BigDecimal getOneUserPayments(Long userId) {
        return paymentServiceClient.getOneUserPayments(userId);
    }
}
