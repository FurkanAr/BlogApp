package com.demo.Blog.controller;

import com.demo.Blog.client.request.PaymentCardGetRequest;
import com.demo.Blog.client.request.PaymentTransferGetRequest;
import com.demo.Blog.client.request.RenewMembershipCardRequest;
import com.demo.Blog.response.MembershipResponse;
import com.demo.Blog.service.MembershipService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/memberships")
public class MembershipController {

    private final MembershipService membershipService;

    public MembershipController(MembershipService membershipService) {
        this.membershipService = membershipService;
    }

    @PostMapping("/start/card")
    public ResponseEntity<String> startMembershipPayByCard(@RequestBody PaymentCardGetRequest paymentCardGetRequest) {
        String message = membershipService.createMembershipPayByCard(paymentCardGetRequest);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @PostMapping("/start/transfer")
    public ResponseEntity<String> startMembershipPayByTransfer(@RequestBody PaymentTransferGetRequest paymentTransferGetRequest) {
        String message = membershipService.createMembershipPayByTransfer(paymentTransferGetRequest);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @PostMapping("/renew/card")
    public ResponseEntity<String> renew(@RequestBody RenewMembershipCardRequest renewMembershipRequest) {
        String message = membershipService.renewMembershipCardRequest(renewMembershipRequest);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @PostMapping("/renew/transfer")
    public ResponseEntity<String> renew(@RequestBody PaymentTransferGetRequest paymentTransferGetRequest) {
        String message = membershipService.renewMembershipTransferRequest(paymentTransferGetRequest);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<MembershipResponse>> getAllMemberships() {
        List<MembershipResponse> membershipResponses = membershipService.getAllMemberships();
        return ResponseEntity.ok(membershipResponses);
    }

    @GetMapping("/{membershipId}")
    public ResponseEntity<MembershipResponse> getOneMembership(@PathVariable Long membershipId) {
        MembershipResponse membershipResponse = membershipService.getOneMembership(membershipId);
        return ResponseEntity.ok(membershipResponse);
    }

    @DeleteMapping("/{membershipId}")
    public ResponseEntity<String> deleteMembershipById(@PathVariable Long membershipId) {
        String id = membershipService.deleteMembershipById(membershipId);
        return ResponseEntity.ok(id);
    }


    @GetMapping("/users")
    public ResponseEntity<MembershipResponse> getUserMembership(@RequestParam("userId") Long userId) {
        MembershipResponse membershipResponse = membershipService.getUserMembership(userId);
        return ResponseEntity.ok(membershipResponse);
    }


}
