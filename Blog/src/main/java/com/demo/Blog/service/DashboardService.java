package com.demo.Blog.service;

import com.demo.Blog.client.PaymentServiceClient;
import com.demo.Blog.client.response.PaymentResponse;
import com.demo.Blog.response.MembershipResponse;
import com.demo.Blog.response.PostResponse;
import com.demo.Blog.response.UserResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    Logger logger = LoggerFactory.getLogger(getClass());

    public DashboardService(UserService userService, PostService postService, MembershipService membershipService, PaymentServiceClient paymentServiceClient) {
        this.userService = userService;
        this.postService = postService;
        this.membershipService = membershipService;
        this.paymentServiceClient = paymentServiceClient;
    }
    public List<UserResponse> getAllUsers() {
        logger.debug("getAllUsers method started");
        logger.info("getAllUsers method successfully worked");
        return userService.findAll();
    }

    public UserResponse getUserById(Long userId) {
        logger.debug("getUserById method started");
        logger.info("getUserById method successfully worked");
        return userService.findById(userId);
    }

    public Long getTotalNumberOfUser() {
        logger.debug("getTotalNumberOfUser method started");
        logger.info("getTotalNumberOfUser method successfully worked");
        return userService.findNumberOfUsers();
    }

    public List<PostResponse> getAllPosts(Optional<Long> userId) {
        logger.debug("getAllPosts method started");
        logger.info("getAllPosts method successfully worked");
        return postService.getAllPosts(userId);
    }

    public List<MembershipResponse> getAllMemberships() {
        logger.debug("getAllMemberships method started");
        logger.info("getAllMemberships method successfully worked");
        return membershipService.getAllMemberships();
    }

    public MembershipResponse getOneMembership(Long membershipId) {
        logger.debug("getOneMembership method started");
        logger.info("getOneMembership method successfully worked");
        return membershipService.getOneMembership(membershipId);
    }

    public Long getTotalNumberOfMemberships() {
        logger.debug("getTotalNumberOfMemberships method started");
        logger.info("getTotalNumberOfMemberships method successfully worked");
        return membershipService.getTotalNumberOfMemberships();
    }

    public List<PaymentResponse> getAllPayments() {
        logger.debug("getAllPayments method started");
        logger.info("getAllPayments method successfully worked");
        return paymentServiceClient.getAllPayments();
    }

    public PaymentResponse getOnePayment(Long paymentId) {
        logger.debug("getOnePayment method started");
        logger.info("getOnePayment method successfully worked");
        return paymentServiceClient.getOnePayment(paymentId);
    }

    public BigDecimal getTotalPaymentPrice() {
        logger.debug("getTotalPaymentPrice method started");
        logger.info("getTotalPaymentPrice method successfully worked");
        return paymentServiceClient.getTotalPrice();
    }

    public BigDecimal getOneUserPayments(Long userId) {
        logger.debug("getOneUserPayments method started");
        logger.info("getOneUserPayments method successfully worked");
        return paymentServiceClient.getOneUserPayments(userId);
    }
}
