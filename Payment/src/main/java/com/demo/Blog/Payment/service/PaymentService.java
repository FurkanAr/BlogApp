package com.demo.Blog.Payment.service;

import com.demo.Blog.Payment.converter.PaymentConverter;
import com.demo.Blog.Payment.model.Payment;
import com.demo.Blog.Payment.model.enums.PaymentStatus;
import com.demo.Blog.Payment.repository.PaymentRepository;
import com.demo.Blog.Payment.request.PaymentCarSendRequest;
import com.demo.Blog.Payment.request.PaymentTransferSendRequest;
import com.demo.Blog.Payment.response.PaymentResponse;
import com.demo.Blog.Payment.utils.PaymentUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService {

    private static final String MESSAGE = "Payment successful, Your subscription has been started";

    private final PaymentRepository paymentRepository;
    private final PaymentConverter paymentConverter;

    public PaymentService(PaymentRepository paymentRepository, PaymentConverter paymentConverter) {
        this.paymentRepository = paymentRepository;
        this.paymentConverter = paymentConverter;
    }

    public PaymentResponse createPayment(PaymentCarSendRequest paymentCarSendRequest) {
        Payment payment = paymentConverter.convert(paymentCarSendRequest);
        if(PaymentUtil.cardPaymentControl(paymentCarSendRequest)){
            changePaymentStatusAndMessage(payment);
            return paymentConverter.convert(paymentRepository.save(payment));
        }
        return paymentConverter.convert(payment);
    }

    public PaymentResponse createPayment(PaymentTransferSendRequest paymentTransferSendRequest) {
        Payment payment = paymentConverter.convert(paymentTransferSendRequest);
        if(PaymentUtil.transferPaymentControl(paymentTransferSendRequest)){
            changePaymentStatusAndMessage(payment);
            return paymentConverter.convert(paymentRepository.save(payment));
        }
        return paymentConverter.convert(payment);
    }

    public List<PaymentResponse> getAllPayments() {
        List<Payment> payments =  paymentRepository.findAll();
        System.out.println(payments);
        List<PaymentResponse> paymentResponses =  paymentConverter.convert(payments);
        System.out.println(paymentResponses);
        return paymentResponses;
    }

    private static void changePaymentStatusAndMessage(Payment payment) {
        payment.setMessage(MESSAGE);
        payment.setStatus(PaymentStatus.ACCEPTED);
    }

}
