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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentConverter paymentConverter;

    Logger logger = LoggerFactory.getLogger(getClass());

    public PaymentService(PaymentRepository paymentRepository, PaymentConverter paymentConverter) {
        this.paymentRepository = paymentRepository;
        this.paymentConverter = paymentConverter;
    }
    @Transactional
    public PaymentResponse createPayment(PaymentCarSendRequest paymentCarSendRequest) {
        logger.info("createPayment method started");
        Payment payment = paymentConverter.convert(paymentCarSendRequest);
        boolean isSuccess = PaymentUtil.cardPaymentControl(paymentCarSendRequest);

        if (isSuccess) {
            return getPaymentResponse(payment);
        }
        logger.info("createPayment method successfully worked");
        return paymentConverter.convert(payment);
    }

    @Transactional
    public PaymentResponse createPayment(PaymentTransferSendRequest paymentTransferSendRequest) {
        logger.info("createPayment method started");
        Payment payment = paymentConverter.convert(paymentTransferSendRequest);
        boolean isSuccess = PaymentUtil.transferPaymentControl(paymentTransferSendRequest);

        if (isSuccess) {
            return getPaymentResponse(payment);
        }
        logger.info("createPayment method successfully worked");
        return paymentConverter.convert(payment);
    }

    private PaymentResponse getPaymentResponse(Payment payment) {
        PaymentUtil.changePaymentStatusAndMessage(payment);
        Payment savedPayment = paymentRepository.save(payment);
        logger.info("Payment created: {}" , payment.getId());
        return paymentConverter.convert(savedPayment);
    }

    public List<PaymentResponse> getAllPayments() {
        logger.info("getAllPayments method started");
        List<Payment> payments = paymentRepository.findAll();
        logger.info("getAllPayments method successfully worked");
        return paymentConverter.convert(payments);
    }

    public PaymentResponse getOnePayment(Long paymentId) {
        logger.info("getOnePayment method started");

        Payment payment = paymentRepository.findById(paymentId).orElseThrow(() ->
                new PaymentNotFoundException(Messages.Payment.ID_NOT_EXISTS + paymentId));

        logger.info("Found payment by paymentId: {} ", paymentId);

        logger.info("getOnePayment method successfully worked");
        return paymentConverter.convert(payment);
    }

    public BigDecimal getTotalPrice() {
        logger.info("getTotalPrice method started");

        BigDecimal total = paymentRepository.sumTotal();;
        logger.info("Total price: {} ", total);

        logger.info("getTotalPrice method successfully worked");
        return total;
    }


    public BigDecimal getOneUserPayment(Long userId) {
        logger.info("getOneUserPayment method started");

        BigDecimal price = paymentRepository.sumByUserId(userId);
        logger.info("Total price: {}, for user: {} ", price, userId);

        logger.info("getOneUserPayment method successfully worked");
        return price;
    }
}
