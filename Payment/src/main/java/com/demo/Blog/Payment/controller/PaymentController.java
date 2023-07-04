package com.demo.Blog.Payment.controller;

import com.demo.Blog.Payment.request.PaymentCarSendRequest;
import com.demo.Blog.Payment.request.PaymentTransferSendRequest;
import com.demo.Blog.Payment.response.PaymentResponse;
import com.demo.Blog.Payment.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    Logger logger = LoggerFactory.getLogger(getClass());

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping(value = "/card")
    public ResponseEntity<PaymentResponse> payByCard(@RequestBody PaymentCarSendRequest paymentCarSendRequest){
        logger.info("payByCard method started");
        PaymentResponse paymentResponse = paymentService.createPayment(paymentCarSendRequest);
        logger.info("payByCard successfully worked, userId: {}", paymentCarSendRequest.getUserId());
        return new ResponseEntity<>(paymentResponse, HttpStatus.CREATED);
    }

    @PostMapping(value = "/transfer")
    public ResponseEntity<PaymentResponse> payByTransfer(@RequestBody PaymentTransferSendRequest paymentTransferSendRequest){
        logger.info("payByTransfer method started");
        PaymentResponse paymentResponse = paymentService.createPayment(paymentTransferSendRequest);
        logger.info("payByTransfer successfully worked, userId: {}", paymentTransferSendRequest.getUserId());
        return new ResponseEntity<>(paymentResponse, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PaymentResponse>> getAllPayments(){
        logger.info("getAllPayments method started");
        List<PaymentResponse> paymentResponses = paymentService.getAllPayments();
        logger.info("getAllPayments successfully worked");
        return ResponseEntity.ok(paymentResponses);
    }

    @GetMapping({"/{paymentId}"})
    public ResponseEntity<PaymentResponse> getOnePayment(@PathVariable Long paymentId){
        logger.info("getOnePayment method started");
        PaymentResponse paymentResponse = paymentService.getOnePayment(paymentId);
        logger.info("getOnePayment successfully worked, paymentId: {}", paymentId);
        return ResponseEntity.ok(paymentResponse);
    }

    @GetMapping({"/total"})
    public ResponseEntity<BigDecimal> getTotalPrice(){
        logger.info("getTotalPrice method started");
        BigDecimal total = paymentService.getTotalPrice();
        logger.info("getTotalPrice successfully worked, total: {}", total);
        return ResponseEntity.ok(total);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<BigDecimal> getOneUserPayments(@PathVariable Long userId){
        logger.info("getOnePayment method started");
        BigDecimal price = paymentService.getOneUserPayment(userId);
        logger.info("getOnePayment successfully worked, userId: {}, price: {}", userId, price);
        return ResponseEntity.ok(price);
    }

}
