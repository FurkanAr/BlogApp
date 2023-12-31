package com.demo.Blog.client;

import com.demo.Blog.client.request.PaymentCardSendRequest;
import com.demo.Blog.client.request.PaymentTransferSendRequest;
import com.demo.Blog.client.response.PaymentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.List;

@FeignClient(value = "Payment", url = "http://localhost:8081")
public interface PaymentServiceClient {

    @PostMapping(value = "/payments/card")
    public PaymentResponse create(@RequestBody PaymentCardSendRequest paymentCardSendRequest);

    @PostMapping(value ="/payments/transfer")
    public PaymentResponse create(@RequestBody PaymentTransferSendRequest paymentTransferSendRequest);

    @GetMapping(value = "/payments")
    public List<PaymentResponse> getAllPayments();

    @GetMapping(value = "/payments/{paymentId}")
    public PaymentResponse getOnePayment(@PathVariable Long paymentId);

    @GetMapping({"/payments/total"})
    public BigDecimal getTotalPrice();

    @GetMapping("/payments/users/{userId}")
    public BigDecimal getOneUserPayments(@PathVariable Long userId);

}
