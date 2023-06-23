package com.demo.Blog.Payment.service;

import com.demo.Blog.Payment.converter.PaymentConverter;
import com.demo.Blog.Payment.exception.PaymentNotFoundException;
import com.demo.Blog.Payment.exception.messages.Messages;
import com.demo.Blog.Payment.model.Payment;
import com.demo.Blog.Payment.model.enums.PaymentStatus;
import com.demo.Blog.Payment.repository.PaymentRepository;
import com.demo.Blog.Payment.request.PaymentCarSendRequest;
import com.demo.Blog.Payment.request.PaymentTransferSendRequest;
import com.demo.Blog.Payment.response.PaymentResponse;
import com.demo.Blog.Payment.utils.PaymentUtil;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PaymentService {


    private final PaymentRepository paymentRepository;
    private final PaymentConverter paymentConverter;

    public PaymentService(PaymentRepository paymentRepository, PaymentConverter paymentConverter) {
        this.paymentRepository = paymentRepository;
        this.paymentConverter = paymentConverter;
    }

    public PaymentResponse createPayment(PaymentCarSendRequest paymentCarSendRequest) {
        Payment payment = paymentConverter.convert(paymentCarSendRequest);
        boolean isSuccess = PaymentUtil.cardPaymentControl(paymentCarSendRequest);
        if (isSuccess) {
            PaymentUtil.changePaymentStatusAndMessage(payment);
            return paymentConverter.convert(paymentRepository.save(payment));
        }
        return paymentConverter.convert(payment);
    }

    public PaymentResponse createPayment(PaymentTransferSendRequest paymentTransferSendRequest) {
        Payment payment = paymentConverter.convert(paymentTransferSendRequest);
        boolean isSuccess = PaymentUtil.transferPaymentControl(paymentTransferSendRequest);
        if (isSuccess) {
            PaymentUtil.changePaymentStatusAndMessage(payment);
            return paymentConverter.convert(paymentRepository.save(payment));
        }
        return paymentConverter.convert(payment);
    }

    public List<PaymentResponse> getAllPayments() {
        List<Payment> payments = paymentRepository.findAll();
        List<PaymentResponse> paymentResponses = paymentConverter.convert(payments);
        return paymentResponses;
    }

    public PaymentResponse getOnePayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId).orElseThrow(() ->
                new PaymentNotFoundException(Messages.Payment.ID_NOT_EXISTS + paymentId));
        return paymentConverter.convert(payment);
    }

    public BigDecimal getTotalPrice() {
        return paymentRepository.sumTotal();
    }


    public BigDecimal getOneUserPayment(Long userId) {
        return paymentRepository.sumByUserId(userId);
    }
}
