package com.demo.Blog.controller;

import com.demo.Blog.client.response.PaymentResponse;
import com.demo.Blog.response.MembershipResponse;
import com.demo.Blog.response.PostResponse;
import com.demo.Blog.response.UserResponse;
import com.demo.Blog.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/dashboard")
public class AdminDashboardController {

    private final DashboardService dashboardService;

    public AdminDashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> userResponses = dashboardService.getAllUsers();
        return ResponseEntity.ok(userResponses);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long userId) {
        UserResponse userResponse = dashboardService.getUserById(userId);
        return ResponseEntity.ok(userResponse);
    }

    @GetMapping("/users/total")
    public ResponseEntity<Long> getTotalNumberOfUser(){
        Long total = dashboardService.getTotalNumberOfUser();
        return ResponseEntity.ok(total);
    }

    @GetMapping("/posts")
    public ResponseEntity<List<PostResponse>> getAllPosts(@RequestParam Optional<Long> userId){
        List<PostResponse> postResponse = dashboardService.getAllPosts(userId);
        return ResponseEntity.ok(postResponse);
    }

    @GetMapping("/memberships")
    public ResponseEntity<List<MembershipResponse>> getAllMemberships() {
        List<MembershipResponse> membershipResponses = dashboardService.getAllMemberships();
        return ResponseEntity.ok(membershipResponses);
    }

    @GetMapping("/memberships/{membershipId}")
    public ResponseEntity<MembershipResponse> getOneMembership(@PathVariable Long membershipId) {
        MembershipResponse membershipResponse = dashboardService.getOneMembership(membershipId);
        return ResponseEntity.ok(membershipResponse);
    }

    @GetMapping("/memberships/total")
    public ResponseEntity<Long> getTotalNumberOfMemberships() {
        Long total = dashboardService.getTotalNumberOfMemberships();
        return ResponseEntity.ok(total);
    }

    @GetMapping("/payments")
    public ResponseEntity<List<PaymentResponse>> getAllPayments(){
        List<PaymentResponse> paymentResponses = dashboardService.getAllPayments();
        return ResponseEntity.ok(paymentResponses);
    }

    @GetMapping("/payments/{paymentId}")
    public ResponseEntity<PaymentResponse> getOnePayment(@PathVariable Long paymentId){
        PaymentResponse paymentResponse = dashboardService.getOnePayment(paymentId);
        return ResponseEntity.ok(paymentResponse);
    }

    @GetMapping("/payments/total")
    public ResponseEntity<BigDecimal> getTotalPaymentPrice(){
        BigDecimal total = dashboardService.getTotalPaymentPrice();
        return ResponseEntity.ok(total);
    }

    @GetMapping("/payments/users/{userId}")
    public ResponseEntity<BigDecimal> getOneUserPayments(@PathVariable Long userId){
        BigDecimal total = dashboardService.getOneUserPayments(userId);
        return ResponseEntity.ok(total);
    }


}
