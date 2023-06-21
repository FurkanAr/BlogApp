package com.demo.Blog.client;

import com.demo.Blog.client.request.PaymentCardSendRequest;
import com.demo.Blog.client.request.PaymentTransferSendRequest;
import com.demo.Blog.client.response.PaymentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "Payment", url = "http://localhost:8081")
public interface PaymentServiceClient {

    @PostMapping(value = "/payments/card")
    public PaymentResponse create(@RequestBody PaymentCardSendRequest paymentCardSendRequest);

    @PostMapping(value ="/payments/transfer")
    public PaymentResponse create(@RequestBody PaymentTransferSendRequest paymentTransferSendRequest);


}
