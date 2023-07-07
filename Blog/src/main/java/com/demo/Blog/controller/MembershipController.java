package com.demo.Blog.controller;

import com.demo.Blog.client.request.PaymentCardGetRequest;
import com.demo.Blog.client.request.PaymentTransferGetRequest;
import com.demo.Blog.client.request.RenewMembershipCardRequest;
import com.demo.Blog.client.response.PaymentResponse;
import com.demo.Blog.response.MembershipResponse;
import com.demo.Blog.service.MembershipService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/memberships")
public class MembershipController {

    private final MembershipService membershipService;
    Logger logger = LoggerFactory.getLogger(getClass());

    public MembershipController(MembershipService membershipService) {
        this.membershipService = membershipService;
    }

    @PostMapping("/start/card")
    public ResponseEntity<PaymentResponse> startMembershipPayByCard(@RequestBody @Valid PaymentCardGetRequest paymentCardGetRequest) {
        logger.info("startMembershipPayByCard method started");
        PaymentResponse paymentResponse = membershipService.createMembershipPayByCard(paymentCardGetRequest);
        logger.info("startMembershipPayByCard successfully worked, userId: {}", paymentCardGetRequest.getUserId());
        return new ResponseEntity<>(paymentResponse, HttpStatus.CREATED);
    }

    @PostMapping("/start/transfer")
    public ResponseEntity<PaymentResponse> startMembershipPayByTransfer(@RequestBody @Valid PaymentTransferGetRequest paymentTransferGetRequest) {
        logger.info("startMembershipPayByTransfer method started");
        PaymentResponse paymentResponse = membershipService.createMembershipPayByTransfer(paymentTransferGetRequest);
        logger.info("startMembershipPayByTransfer successfully worked, userId: {}", paymentTransferGetRequest.getUserId());
        return new ResponseEntity<>(paymentResponse, HttpStatus.CREATED);
    }

    @PostMapping("/renew/card")
    public ResponseEntity<PaymentResponse> renewMembershipCardRequest(@RequestBody @Valid RenewMembershipCardRequest renewMembershipRequest) {
        logger.info("renew method started");
        PaymentResponse paymentResponse = membershipService.renewMembershipCardRequest(renewMembershipRequest);
        logger.info("renew successfully worked, userId: {}", renewMembershipRequest.getUserId());
        return new ResponseEntity<>(paymentResponse, HttpStatus.CREATED);
    }

    @PostMapping("/renew/transfer")
    public ResponseEntity<PaymentResponse> renewMembershipTransferRequest(@RequestBody @Valid PaymentTransferGetRequest paymentTransferGetRequest) {
        logger.info("renew method started");
        PaymentResponse paymentResponse = membershipService.renewMembershipTransferRequest(paymentTransferGetRequest);
        logger.info("renew successfully worked, userId: {}", paymentTransferGetRequest.getUserId());
        return new ResponseEntity<>(paymentResponse, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<MembershipResponse>> getAllMemberships() {
        logger.info("getAllMemberships method started");
        List<MembershipResponse> membershipResponses = membershipService.getAllMemberships();
        logger.info("getAllMemberships successfully worked");
        return ResponseEntity.ok(membershipResponses);
    }

    @GetMapping("/{membershipId}")
    public ResponseEntity<MembershipResponse> getOneMembership(@PathVariable Long membershipId) {
        logger.info("getOneMembership method started");
        MembershipResponse membershipResponse = membershipService.getOneMembership(membershipId);
        logger.info("getOneMembership successfully worked, membershipId: {}", membershipId);
        return ResponseEntity.ok(membershipResponse);
    }

    @DeleteMapping("/{membershipId}")
    public ResponseEntity<String> deleteMembershipById(@PathVariable Long membershipId) {
        logger.info("deleteMembershipById method started");
        String id = membershipService.deleteMembershipById(membershipId);
        logger.info("deleteMembershipById successfully worked, membershipId: {}", membershipId);
        return ResponseEntity.ok(id);
    }

    @GetMapping("/users")
    public ResponseEntity<MembershipResponse> getUserMembership(@RequestParam("userId") Long userId) {
        logger.info("getUserMembership method started");
        MembershipResponse membershipResponse = membershipService.getUserMembership(userId);
        logger.info("getUserMembership successfully worked, userId: {}", userId);
        return ResponseEntity.ok(membershipResponse);
    }

}
