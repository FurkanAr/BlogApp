package com.demo.Blog.Payment.service;

import com.demo.Blog.Payment.converter.PaymentConverter;
import com.demo.Blog.Payment.exception.PaymentNotFoundException;
import com.demo.Blog.Payment.model.Payment;
import com.demo.Blog.Payment.model.enums.PaymentStatus;
import com.demo.Blog.Payment.model.enums.PaymentType;
import com.demo.Blog.Payment.repository.PaymentRepository;
import com.demo.Blog.Payment.request.PaymentCarSendRequest;
import com.demo.Blog.Payment.request.PaymentTransferSendRequest;
import com.demo.Blog.Payment.response.PaymentResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
class PaymentServiceTest {

    @InjectMocks
    private PaymentService paymentService;
    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private PaymentConverter paymentConverter;

    @Test
    void it_should_create_refused_payment_pay_by_card() {

        // given
        Mockito.when(paymentConverter.convert(Mockito.any(PaymentCarSendRequest.class))).thenReturn(getRefusedCardPayment());
        Mockito.when(paymentConverter.convert(Mockito.any(Payment.class))).thenReturn(getPaymentCardPaymentRefusedResponse());

        // when
        PaymentResponse paymentResponse = paymentService.createPayment(getRefusedPaymentCardSendRequest());

        //then
        assertThat(paymentResponse).isNotNull();
        assertThat(paymentResponse.getMessage()).isEqualTo(getRefusedCardPayment().getMessage());
        assertThat(paymentResponse.getStatus()).isEqualTo(getRefusedCardPayment().getStatus());
        assertEquals(paymentResponse.getUserId(), getRefusedCardPayment().getUserId());

        verify(paymentRepository, times(0)).save(Mockito.any(Payment.class));
    }

    @Test
    void it_should_create_payment_pay_by_card() {

        // given
        Payment payment = getCardPayment();
        payment.setId(1L);
        Mockito.when(paymentConverter.convert(Mockito.any(PaymentCarSendRequest.class))).thenReturn(new Payment());
        Mockito.when(paymentRepository.save(Mockito.any(Payment.class))).thenReturn(payment);
        Mockito.when(paymentConverter.convert(Mockito.any(Payment.class))).thenReturn(getPaymentCardPaymentAcceptedResponse());

        // when
        PaymentResponse paymentResponse = paymentService.createPayment(getPaymentCardSendRequest());

        //then
        assertThat(paymentResponse).isNotNull();
        assertEquals(paymentResponse.getId(), payment.getId());
        assertEquals(paymentResponse.getUserId(), payment.getUserId());
        assertThat(paymentResponse.getMessage()).isEqualTo(getCardPayment().getMessage());
        assertThat(paymentResponse.getStatus()).isEqualTo(getCardPayment().getStatus());

        verify(paymentRepository).save(Mockito.any(Payment.class));
    }

    @Test
    void it_should_create_refused_payment_pay_by_transfer() {

        // given
        Mockito.when(paymentConverter.convert(Mockito.any(PaymentTransferSendRequest.class))).thenReturn(getRefusedTransferPayment());
        Mockito.when(paymentConverter.convert(Mockito.any(Payment.class))).thenReturn(getPaymentTransferPaymentRefusedResponse());

        // when
        PaymentResponse paymentResponse = paymentService.createPayment(getRefusedPaymentTransferSendRequest());

        //then
        assertThat(paymentResponse).isNotNull();
        assertThat(paymentResponse.getMessage()).isEqualTo(getRefusedTransferPayment().getMessage());
        assertThat(paymentResponse.getStatus()).isEqualTo(getRefusedTransferPayment().getStatus());
        assertEquals(paymentResponse.getUserId(), getRefusedTransferPayment().getUserId());

        verify(paymentRepository, times(0)).save(Mockito.any(Payment.class));
    }

    @Test
    void it_should_create_payment_pay_by_transfer() {

        // given
        Payment payment = getTransferPayment();
        payment.setId(2L);
        Mockito.when(paymentConverter.convert(Mockito.any(PaymentTransferSendRequest.class))).thenReturn(new Payment());
        Mockito.when(paymentRepository.save(Mockito.any(Payment.class))).thenReturn(payment);
        Mockito.when(paymentConverter.convert(Mockito.any(Payment.class))).thenReturn(getPaymentTransferAcceptedResponse());

        // when
        PaymentResponse paymentResponse = paymentService.createPayment(getPaymentTransferSendRequest());

        //then
        assertThat(paymentResponse).isNotNull();
        assertEquals(paymentResponse.getId(), payment.getId());
        assertEquals(paymentResponse.getUserId(), payment.getUserId());
        assertThat(paymentResponse.getMessage()).isEqualTo(getTransferPayment().getMessage());
        assertThat(paymentResponse.getStatus()).isEqualTo(getTransferPayment().getStatus());

        verify(paymentRepository).save(Mockito.any(Payment.class));
    }

    @Test
    void it_should_get_all_payments() {

        // given
        Mockito.when(paymentRepository.findAll()).thenReturn(getAllPayments());
        Mockito.when(paymentConverter.convert(Mockito.anyList())).thenReturn(getAllPaymentResponses());

        // when
        List<PaymentResponse> paymentResponses = paymentService.getAllPayments();

        // then
        assertThat(paymentResponses).isNotNull();
        assertEquals(paymentResponses.get(0).getId(), 1L);
        assertEquals(paymentResponses.get(1).getId(), 2L);
        assertEquals(paymentResponses.get(0).getStatus(), getAllPayments().get(0).getStatus());
        assertEquals(paymentResponses.get(1).getStatus(), getAllPayments().get(1).getStatus());
    }

    @Test
    void it_should_throw_payment_not_found_exception() {

        // given

        // when
        Throwable exception = catchThrowable(() -> paymentService.getOnePayment(1L));

        // then
        assertThat(exception).isInstanceOf(PaymentNotFoundException.class);
    }

    @Test
    void it_should_get_one_payment() {

        // given
        Payment payment = getCardPayment();
        payment.setId(1L);
        Mockito.when(paymentRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(payment));
        Mockito.when(paymentConverter.convert(Mockito.any(Payment.class))).thenReturn(getPaymentCardPaymentAcceptedResponse());

        // when
        PaymentResponse paymentResponse = paymentService.getOnePayment(1L);

        // given
        assertThat(paymentResponse).isNotNull();
        assertEquals(paymentResponse.getId(), payment.getId());
        assertEquals(paymentResponse.getUserId(), payment.getUserId());
    }

    @Test
    void it_should_get_total_price() {

        // given
        Mockito.when(paymentRepository.sumTotal()).thenReturn(BigDecimal.valueOf(30000));

        // when
        BigDecimal response = paymentService.getTotalPrice();

        // then
        assertThat(response).isNotNull();
        assertEquals(response, BigDecimal.valueOf(30000));
    }

    @Test
    void it_should_get_one_user_payment() {

        // given
        Mockito.when(paymentRepository.sumByUserId(Mockito.anyLong()))
                .thenReturn(BigDecimal.valueOf(75.45));

        // when
        BigDecimal response  = paymentService.getOneUserPayment(2L);

        // then
        assertThat(response).isNotNull();
        assertEquals(response, BigDecimal.valueOf(75.45));
    }

    private List<PaymentResponse> getAllPaymentResponses() {
        return List.of(getPaymentCardPaymentAcceptedResponse(), getPaymentTransferAcceptedResponse());
    }

    private List<Payment> getAllPayments() {
        return List.of(getCardPayment(), getTransferPayment());
    }

    private PaymentTransferSendRequest getPaymentTransferSendRequest() {
        return new PaymentTransferSendRequest ("TR 000212564899147532", "TR 090002562311458900", "MEDIUM", 2L, "tester");
    }

    private Payment getCardPayment() {
        return new Payment(LocalDateTime.now(), 2L, "tester", PaymentType.CARD, 39.99, "No information", "4441113332", PaymentStatus.ACCEPTED, "Payment successful, Enjoy!!");
    }

    private Payment getTransferPayment() {
        return new Payment(LocalDateTime.now(), 2L, "tester", PaymentType.TRANSFER, 39.99, "TR 000212564899147532", "No information", PaymentStatus.ACCEPTED, "Payment successful, Enjoy!!");
    }

    private PaymentCarSendRequest getRefusedPaymentCardSendRequest() {
        return new PaymentCarSendRequest("4441113332", LocalDate.now(Clock.systemDefaultZone()), "131", 1L, "tester");
    }

    private PaymentCarSendRequest getPaymentCardSendRequest() {
        return new PaymentCarSendRequest("4441113332", LocalDate.now(Clock.systemDefaultZone()).plusYears(10), "131", 1L, "tester");
    }

    private PaymentResponse getPaymentCardPaymentAcceptedResponse() {
        return new PaymentResponse(1L, "Payment successful, Enjoy!!", 2L, PaymentType.CARD, PaymentStatus.ACCEPTED);
    }

    private PaymentResponse getPaymentCardPaymentRefusedResponse() {
        return new PaymentResponse(null, "Payment failed, try again!", 2L, PaymentType.CARD, PaymentStatus.REFUSED);
    }

    private PaymentResponse getPaymentTransferPaymentRefusedResponse() {
        return new PaymentResponse(2L, "Payment failed, try again!", 2L, PaymentType.TRANSFER, PaymentStatus.REFUSED);
    }

    private PaymentResponse getPaymentTransferAcceptedResponse() {
        return new PaymentResponse(2L, "Payment successful, Enjoy!!", 2L, PaymentType.TRANSFER, PaymentStatus.ACCEPTED);
    }

    private PaymentTransferSendRequest getRefusedPaymentTransferSendRequest() {
        return new PaymentTransferSendRequest ("000212564899147532", "TR 090002562311458900", "MEDIUM", 2L, "tester");
    }

    private Payment getRefusedCardPayment() {
        return new Payment(LocalDateTime.now(), 2L, "tester", PaymentType.CARD, 39.99, "No information", "4441113332", PaymentStatus.REFUSED, "Payment failed, try again!");
    }

    private Payment getRefusedTransferPayment() {
        return new Payment(LocalDateTime.now(), 2L, "tester", PaymentType.TRANSFER, 39.99, "TR 000212564899147532", "No information", PaymentStatus.REFUSED, "Payment failed, try again!");
    }

}