package com.demo.Blog.controller;

import com.demo.Blog.client.response.PaymentResponse;
import com.demo.Blog.response.MembershipResponse;
import com.demo.Blog.response.PostResponse;
import com.demo.Blog.response.UserResponse;
import com.demo.Blog.service.DashboardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/dashboard")
public class AdminDashboardController {
    private final DashboardService dashboardService;
    Logger logger = LoggerFactory.getLogger(getClass());

    public AdminDashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        logger.debug("getAllUsers method started");
        List<UserResponse> userResponses = dashboardService.getAllUsers();
        logger.info("getAllUsers method successfully worked");
        return ResponseEntity.ok(userResponses);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long userId) {
        logger.debug("getUser method started");
        UserResponse userResponse = dashboardService.getUserById(userId);
        logger.info("getOneUser successfully worked, userId: {}", userId);
        return ResponseEntity.ok(userResponse);
    }

    @GetMapping("/users/total")
    public ResponseEntity<Long> getTotalNumberOfUser() {
        logger.debug("getTotalNumberOfUser method started");
        Long total = dashboardService.getTotalNumberOfUser();
        logger.info("getOneUser successfully worked, total user: {}", total);
        return ResponseEntity.ok(total);
    }

    @GetMapping("/posts")
    public ResponseEntity<List<PostResponse>> getAllPosts(@RequestParam Optional<Long> userId) {
        logger.debug("getAllPosts method started");
        List<PostResponse> postResponse = dashboardService.getAllPosts(userId);
        logger.info("getAllPost successfully worked, number of post: {}", postResponse.size());
        return ResponseEntity.ok(postResponse);
    }

    @GetMapping("/memberships")
    public ResponseEntity<List<MembershipResponse>> getAllMemberships() {
        logger.debug("getAllMemberships method started");
        List<MembershipResponse> membershipResponses = dashboardService.getAllMemberships();
        logger.info("getAllMemberships successfully worked");
        return ResponseEntity.ok(membershipResponses);
    }

    @GetMapping("/memberships/{membershipId}")
    public ResponseEntity<MembershipResponse> getOneMembership(@PathVariable Long membershipId) {
        logger.debug("getOneMembership method started");
        MembershipResponse membershipResponse = dashboardService.getOneMembership(membershipId);
        logger.info("getOneMembership successfully worked, membershipId: {}", membershipId);
        return ResponseEntity.ok(membershipResponse);
    }

    @GetMapping("/memberships/total")
    public ResponseEntity<Long> getTotalNumberOfMemberships() {
        logger.debug("getTotalNumberOfMemberships method started");
        Long total = dashboardService.getTotalNumberOfMemberships();
        logger.info("getTotalNumberOfMemberships successfully worked, number of memberships: {}", total);
        return ResponseEntity.ok(total);
    }

    @GetMapping("/payments")
    public ResponseEntity<List<PaymentResponse>> getAllPayments() {
        logger.debug("getAllPayments method started");
        List<PaymentResponse> paymentResponses = dashboardService.getAllPayments();
        logger.info("getAllPayments successfully worked");
        return ResponseEntity.ok(paymentResponses);
    }

    @GetMapping("/payments/{paymentId}")
    public ResponseEntity<PaymentResponse> getOnePayment(@PathVariable Long paymentId) {
        logger.debug("getOnePayment method started");
        PaymentResponse paymentResponse = dashboardService.getOnePayment(paymentId);
        logger.info("getOnePayment successfully worked, paymentId: {}", paymentId);
        return ResponseEntity.ok(paymentResponse);
    }

    @GetMapping("/payments/total")
    public ResponseEntity<BigDecimal> getTotalPaymentPrice() {
        logger.debug("getTotalPaymentPrice method started");
        BigDecimal total = dashboardService.getTotalPaymentPrice();
        logger.info("getTotalPaymentPrice successfully worked, total payments: {}", total);
        return ResponseEntity.ok(total);
    }

    @GetMapping("/payments/users/{userId}")
    public ResponseEntity<BigDecimal> getOneUserPayments(@PathVariable Long userId) {
        logger.debug("getOneUserPayments method started");
        BigDecimal total = dashboardService.getOneUserPayments(userId);
        logger.info("getOneUserPayments successfully worked, userId: {}", userId);
        return ResponseEntity.ok(total);
    }

}
