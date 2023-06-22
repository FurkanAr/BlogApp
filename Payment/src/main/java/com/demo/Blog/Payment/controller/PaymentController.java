package com.demo.Blog.Payment.controller;

import com.demo.Blog.Payment.request.PaymentCarSendRequest;
import com.demo.Blog.Payment.request.PaymentTransferSendRequest;
import com.demo.Blog.Payment.response.PaymentResponse;
import com.demo.Blog.Payment.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }


    @PostMapping(value = "/card")
    public ResponseEntity<PaymentResponse> payByCard(@RequestBody PaymentCarSendRequest paymentCarSendRequest){
        return new ResponseEntity<>(paymentService.createPayment(paymentCarSendRequest), HttpStatus.CREATED);
    }

    @PostMapping(value = "/transfer")
    public ResponseEntity<PaymentResponse> payByTransfer(@RequestBody PaymentTransferSendRequest paymentTransferSendRequest){
        return new ResponseEntity<>(paymentService.createPayment(paymentTransferSendRequest), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PaymentResponse>> getAllPayments(){
        return new ResponseEntity<>(paymentService.getAllPayments(), HttpStatus.OK);
    }

    @GetMapping({"/{paymentId}"})
    public ResponseEntity<PaymentResponse> getOnePayment(@PathVariable Long paymentId){
        return new ResponseEntity<>(paymentService.getOnePayment(paymentId), HttpStatus.OK);
    }

    @GetMapping({"/total"})
    public ResponseEntity<BigDecimal> getTotalPrice(){
        return new ResponseEntity<>(paymentService.getTotalPrice(), HttpStatus.OK);
    }


}
