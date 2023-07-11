package com.demo.Blog.service;

import com.demo.Blog.client.PaymentServiceClient;
import com.demo.Blog.client.converter.PaymentConverter;
import com.demo.Blog.client.model.enums.PaymentStatus;
import com.demo.Blog.client.model.enums.PaymentType;
import com.demo.Blog.client.request.*;
import com.demo.Blog.client.response.PaymentResponse;
import com.demo.Blog.config.rabbitMQ.RabbitMQMailConfiguration;
import com.demo.Blog.converter.MailConverter;
import com.demo.Blog.converter.MembershipConverter;
import com.demo.Blog.exception.membership.MembershipNotFoundByUserIdException;
import com.demo.Blog.exception.membership.MembershipNotFoundException;
import com.demo.Blog.exception.membership.UserHasMembershipException;
import com.demo.Blog.exception.payment.PaymentRefusedException;
import com.demo.Blog.model.CardInfo;
import com.demo.Blog.model.Membership;
import com.demo.Blog.model.User;
import com.demo.Blog.model.enums.MembershipStatus;
import com.demo.Blog.model.enums.UserRole;
import com.demo.Blog.repository.MembershipRepository;
import com.demo.Blog.request.MailRequest;
import com.demo.Blog.response.MembershipResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
class MembershipServiceTest {

    @InjectMocks
    private MembershipService membershipService;
    @Mock
    private UserService userService;
    @Mock
    private MembershipRepository membershipRepository;
    @Mock
    private MembershipConverter membershipConverter;
    @Mock
    private PaymentConverter paymentConverter;
    @Mock
    private PaymentServiceClient paymentServiceClient;
    @Mock
    private RabbitMQMailConfiguration rabbitMQMailConfiguration;
    @Mock
    private RabbitTemplate rabbitTemplate;
    @Mock
    private MailConverter mailConverter;
    @Mock
    private CardService cardService;


    @Test
    void it_should_throw_user_not_found_exception_when_user_is_null_pay_by_card() {

        // given

        // when
        Throwable exception = catchThrowable(() -> membershipService.createMembershipPayByCard(new PaymentCardGetRequest()));

        // then
        assertThat(exception).isInstanceOf(NullPointerException.class);
    }

    @Test
    void it_should_throw_user_has_membership_exception_pay_by_card() {

        // given
        User user = getUser();
        user.setId(1L);
        Membership membership = getMembership();
        user.setMembership(membership);

        Mockito.when(userService.findUserById(1L)).thenReturn(user);

        // when
        Throwable exception = catchThrowable(() -> membershipService.createMembershipPayByCard(getPaymentCardRequest()));

        //then
        assertThat(exception).isInstanceOf(UserHasMembershipException.class);
        verify(membershipRepository, times(0)).save(Mockito.any(Membership.class));
    }

    @Test
    void it_should_throw_payment_refused_exception_pay_by_card() {

        // given
        User user = getUser();
        user.setId(1L);
        Mockito.when(userService.findUserById(Mockito.anyLong())).thenReturn(user);
        Mockito.when(paymentConverter.convert(Mockito.any(User.class), Mockito.any(PaymentCardGetRequest.class)))
                .thenReturn(getPaymentCardSendRequest());
        Mockito.when(paymentServiceClient.create(Mockito.any(PaymentCardSendRequest.class))).thenReturn(getPaymentCardPaymentRefusedResponse());

        // when
        Throwable exception = catchThrowable(() -> membershipService.createMembershipPayByCard(getPaymentCardRequest()));

        //then
        assertThat(exception).isInstanceOf(PaymentRefusedException.class);
        verify(membershipRepository, times(0)).save(Mockito.any(Membership.class));
    }

    @Test
    void it_should_create_membership_pay_by_card() {

        // given
        User user = getUser();
        user.setId(1L);
        Membership membership = getMembership();
        membership.setId(1L);

        Mockito.when(userService.findUserById(Mockito.anyLong())).thenReturn(user);
        Mockito.when(paymentConverter.convert(Mockito.any(User.class), Mockito.any(PaymentCardGetRequest.class)))
                .thenReturn(getPaymentCardSendRequest());
        Mockito.when(paymentServiceClient.create(Mockito.any(PaymentCardSendRequest.class)))
                .thenReturn(getPaymentCardPaymentAcceptedResponse());
        Mockito.when(membershipConverter.convert(Mockito.any(User.class))).thenReturn(new Membership());
        Mockito.when(membershipRepository.save(Mockito.any(Membership.class))).thenReturn(membership);
        Mockito.when(mailConverter.convert(Mockito.any(User.class), Mockito.anyString())).thenReturn(getMailRequest());
        Mockito.when(rabbitMQMailConfiguration.getQueueName()).thenReturn("mail.queue.name");

        // when
        PaymentResponse paymentResponse = membershipService.createMembershipPayByCard(getPaymentCardRequest());

        //then
        assertThat(paymentResponse).isNotNull();
        assertThat(paymentResponse.getMessage()).isEqualTo("Payment successful, Enjoy!!");
        assertThat(paymentResponse.getStatus()).isEqualTo(PaymentStatus.ACCEPTED);

        verify(rabbitTemplate, times(1)).convertAndSend(Mockito.anyString(), Mockito.any(MailRequest.class));
        verify(membershipRepository).save(Mockito.any(Membership.class));
    }

    @Test
    void it_should_throw_membership_not_found_exception_when_user_is_null_renew_by_card() {

        // given

        // when
        Throwable exception = catchThrowable(() -> membershipService.renewMembershipCardRequest(new RenewMembershipCardRequest()));

        // then
        assertThat(exception).isInstanceOf(MembershipNotFoundByUserIdException.class);
    }

    @Test
    void it_should_throw_payment_refused_exception_renew_by_card() {

        // given
        User user = getUser();
        user.setId(1L);
        Membership membership = getMembership();
        membership.setId(1L);
        user.setMembership(membership);
        CardInfo card = getCard();
        card.setId(1L);
        card.setUser(user);

        Mockito.when(membershipRepository.findByUserId(Mockito.anyLong())).thenReturn(Optional.of(membership));
        Mockito.when(cardService.getCardByUserId(Mockito.anyLong())).thenReturn(card);
        Mockito.when(paymentConverter.getCardInfoForPayment(Mockito.any(Membership.class), Mockito.any(CardInfo.class)))
                .thenReturn(new PaymentCardSendRequest());
        Mockito.when(paymentServiceClient.create(Mockito.any(PaymentCardSendRequest.class))).thenReturn(getPaymentCardPaymentRefusedResponse());

        // when
        Throwable exception = catchThrowable(() -> membershipService.renewMembershipCardRequest(getRenewMembershipCardRequest()));

        // then
        assertThat(exception).isInstanceOf(PaymentRefusedException.class);
    }

    @Test
    void it_should_renew_membership_renew_by_card() {

        // given
        User user = getUser();
        user.setId(1L);
        Membership membership = getMembership();
        membership.setId(1L);
        membership.setUser(user);
        user.setMembership(membership);
        CardInfo card = getCard();
        card.setId(1L);
        card.setUser(user);

        Mockito.when(membershipRepository.findByUserId(Mockito.anyLong())).thenReturn(Optional.of(membership));
        Mockito.when(cardService.getCardByUserId(Mockito.anyLong())).thenReturn(card);
        Mockito.when(paymentConverter.getCardInfoForPayment(Mockito.any(Membership.class), Mockito.any(CardInfo.class)))
                .thenReturn(new PaymentCardSendRequest());
        Mockito.when(paymentServiceClient.create(Mockito.any(PaymentCardSendRequest.class)))
                .thenReturn(getPaymentCardPaymentAcceptedResponse());
        Mockito.when(membershipRepository.save(Mockito.any(Membership.class))).thenReturn(membership);
        Mockito.when(mailConverter.convert(Mockito.any(User.class), Mockito.anyString())).thenReturn(getMailRequest());
        Mockito.when(rabbitMQMailConfiguration.getQueueName()).thenReturn("mail.queue.name");

        // when
        PaymentResponse paymentResponse = membershipService.renewMembershipCardRequest(getRenewMembershipCardRequest());

        //then
        assertThat(paymentResponse).isNotNull();
        assertThat(paymentResponse.getMessage()).isEqualTo("Payment successful, Enjoy!!");
        assertThat(paymentResponse.getStatus()).isEqualTo(PaymentStatus.ACCEPTED);
        assertThat(membership.getExpireDate()).isEqualTo(LocalDate.now().plusMonths(1));

        verify(rabbitTemplate, times(1)).convertAndSend(Mockito.anyString(), Mockito.any(MailRequest.class));
        verify(membershipRepository).save(Mockito.any(Membership.class));
    }

    @Test
    void it_should_throw_user_not_found_exception_when_user_is_null_pay_by_transfer() {

        // given

        // when
        Throwable exception = catchThrowable(() -> membershipService.createMembershipPayByTransfer(new PaymentTransferGetRequest()));

        // then
        assertThat(exception).isInstanceOf(NullPointerException.class);
    }

    @Test
    void it_should_throw_user_has_membership_exception_pay_by_transfer() {

        // given
        User user = getUser();
        user.setId(1L);
        Membership membership = getMembership();
        user.setMembership(membership);
        Mockito.when(userService.findUserById(1L)).thenReturn(user);

        // when
        Throwable exception = catchThrowable(() -> membershipService.createMembershipPayByTransfer(getPaymentTransferRequest()));

        //then
        assertThat(exception).isInstanceOf(UserHasMembershipException.class);
        verify(membershipRepository, times(0)).save(Mockito.any(Membership.class));
    }

    @Test
    void it_should_throw_payment_refused_exception_pay_by_transfer() {

        // given
        User user = getUser();
        user.setId(1L);
        Mockito.when(userService.findUserById(Mockito.anyLong())).thenReturn(user);
        Mockito.when(paymentConverter.convert(Mockito.any(User.class), Mockito.any(PaymentTransferGetRequest.class)))
                .thenReturn(getPaymentTransferSendRequest());
        Mockito.when(paymentServiceClient.create(Mockito.any(PaymentTransferSendRequest.class))).thenReturn(getPaymentTransferPaymentRefusedResponse());

        // when
        Throwable exception = catchThrowable(() -> membershipService.createMembershipPayByTransfer(getPaymentTransferRequest()));

        //then
        assertThat(exception).isInstanceOf(PaymentRefusedException.class);
        verify(membershipRepository, times(0)).save(Mockito.any(Membership.class));
    }

    @Test
    void it_should_create_membership_pay_by_transfer() {

        // given
        User user = getUser();
        user.setId(1L);
        Membership membership = getMembership();
        membership.setId(1L);

        Mockito.when(userService.findUserById(Mockito.anyLong())).thenReturn(user);
        Mockito.when(paymentConverter.convert(Mockito.any(User.class), Mockito.any(PaymentTransferGetRequest.class)))
                .thenReturn(getPaymentTransferSendRequest());
        Mockito.when(paymentServiceClient.create(Mockito.any(PaymentTransferSendRequest.class))).thenReturn(getPaymentTransferAcceptedResponse());
        Mockito.when(membershipConverter.convert(Mockito.any(User.class))).thenReturn(new Membership());
        Mockito.when(membershipRepository.save(Mockito.any(Membership.class))).thenReturn(membership);
        Mockito.when(mailConverter.convert(Mockito.any(User.class), Mockito.anyString())).thenReturn(getMailRequest());
        Mockito.when(rabbitMQMailConfiguration.getQueueName()).thenReturn("mail.queue.name");

        // when
        PaymentResponse paymentResponse = membershipService.createMembershipPayByTransfer(getPaymentTransferRequest());

        //then
        assertThat(paymentResponse).isNotNull();
        assertThat(paymentResponse.getMessage()).isEqualTo("Payment successful, Enjoy!!");
        assertThat(paymentResponse.getStatus()).isEqualTo(PaymentStatus.ACCEPTED);
        assertEquals(paymentResponse.getId(), 2L);

        verify(rabbitTemplate, times(1)).convertAndSend(Mockito.anyString(), Mockito.any(MailRequest.class));
        verify(membershipRepository).save(Mockito.any(Membership.class));
    }

    @Test
    void it_should_throw_user_not_found_exception_when_user_is_null_renew_by_transfer() {

        // given

        // when
        Throwable exception = catchThrowable(() -> membershipService.renewMembershipTransferRequest(new PaymentTransferGetRequest()));

        // then
        assertThat(exception).isInstanceOf(MembershipNotFoundByUserIdException.class);
    }

    @Test
    void it_should_throw_payment_refused_exception_renew_by_transfer() {

        // given
        User user = getUser();
        user.setId(1L);
        Membership membership = getMembership();
        membership.setId(1L);
        user.setMembership(membership);

        Mockito.when(membershipRepository.findByUserId(Mockito.anyLong())).thenReturn(Optional.of(membership));
        Mockito.when(paymentConverter.convert(Mockito.any(User.class), Mockito.any(PaymentTransferGetRequest.class)))
                .thenReturn(new PaymentTransferSendRequest());
        Mockito.when(paymentServiceClient.create(Mockito.any(PaymentTransferSendRequest.class))).thenReturn(getPaymentTransferPaymentRefusedResponse());

        // when
        Throwable exception = catchThrowable(() -> membershipService.renewMembershipTransferRequest(getPaymentTransferRequest()));

        // then
        assertThat(exception).isInstanceOf(PaymentRefusedException.class);
    }

    @Test
    void it_should_renew_membership_pay_by_transfer() {

        // given
        User user = getUser();
        user.setId(1L);
        Membership membership = getMembership();
        membership.setId(1L);

        Mockito.when(membershipRepository.findByUserId(Mockito.anyLong())).thenReturn(Optional.of(membership));
        Mockito.when(paymentConverter.convert(Mockito.any(User.class), Mockito.any(PaymentTransferGetRequest.class)))
                .thenReturn(new PaymentTransferSendRequest());
        Mockito.when(paymentServiceClient.create(Mockito.any(PaymentTransferSendRequest.class))).thenReturn(getPaymentTransferAcceptedResponse());
        Mockito.when(membershipRepository.save(Mockito.any(Membership.class))).thenReturn(membership);
        Mockito.when(mailConverter.convert(Mockito.any(User.class), Mockito.anyString())).thenReturn(getMailRequest());
        Mockito.when(rabbitMQMailConfiguration.getQueueName()).thenReturn("mail.queue.name");

        // when
        PaymentResponse paymentResponse = membershipService.renewMembershipTransferRequest(getPaymentTransferRequest());

        //then
        assertThat(paymentResponse).isNotNull();
        assertThat(paymentResponse.getMessage()).isEqualTo("Payment successful, Enjoy!!");
        assertThat(paymentResponse.getStatus()).isEqualTo(PaymentStatus.ACCEPTED);
        assertThat(membership.getExpireDate()).isEqualTo(LocalDate.now().plusMonths(1));

        verify(rabbitTemplate, times(1)).convertAndSend(Mockito.anyString(), Mockito.any(MailRequest.class));
        verify(membershipRepository).save(Mockito.any(Membership.class));
    }

    @Test
    void it_should_throw_user_not_found_exception_when_user_is_null_delete_membership_by_id() {

        // given

        // when
        Throwable exception = catchThrowable(() -> membershipService.deleteMembershipById(1L));

        // then
        assertThat(exception).isInstanceOf(MembershipNotFoundException.class);
    }

    @Test
    void it_should_delete_membership_by_id() {

        // given
        Membership membership = getMembership();
        membership.setId(1L);
        Mockito.when(membershipRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(membership));

        // when
        String response = membershipService.deleteMembershipById(1L);

        // then
        assertThat(response).isNotNull();
        assertThat(response).isEqualTo(membership.getId().toString());
        Mockito.verify(membershipRepository, times(1)).delete(Mockito.any(Membership.class));
    }

    @Test
    void it_should_get_all_memberships() {

        // given
        Mockito.when(membershipRepository.findAll()).thenReturn(getMemberships());
        Mockito.when(membershipConverter.convert(Mockito.anyList())).thenReturn(getAllMembershipResponses());

        // when
        List<MembershipResponse> membershipResponses = membershipService.getAllMemberships();

        assertThat(membershipResponses).isNotNull();
        assertEquals(membershipResponses.get(0).getId(), 1L);
        assertEquals(membershipResponses.get(1).getId(), 2L);
        assertEquals(membershipResponses.get(0).getStatus(), getMemberships().get(0).getStatus());
        assertEquals(membershipResponses.get(1).getStatus(), getMemberships().get(1).getStatus());
    }

    @Test
    void it_should_throw_user_not_found_exception_when_user_is_null_get_one_membership() {

        // given

        // when
        Throwable exception = catchThrowable(() -> membershipService.getOneMembership(1L));

        // then
        assertThat(exception).isInstanceOf(MembershipNotFoundException.class);
    }

    @Test
    void it_should_get_one_membership() {

        // given
        Membership membership = getMembership();
        membership.setId(1L);
        Mockito.when(membershipRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(membership));
        Mockito.when(membershipConverter.convert(Mockito.any(Membership.class)))
                .thenReturn(getMembershipResponse(1L));

        // when
        MembershipResponse membershipResponse = membershipService.getOneMembership(1L);

        // then
        assertThat(membershipResponse).isNotNull();
        assertEquals(membershipResponse.getId(), membership.getId());
    }

    @Test
    void it_should_throw_user_not_found_exception_when_user_is_null_get_user_membership() {

        // given

        // when
        Throwable exception = catchThrowable(() -> membershipService.getUserMembership(1L));

        // then
        assertThat(exception).isInstanceOf(MembershipNotFoundByUserIdException.class);
    }

    @Test
    void it_should_get_user_membership() {

        // given
        Membership membership = getMembership();
        membership.setId(1L);
        User user = getUser();
        user.setId(1L);
        membership.setUser(user);

        Mockito.when(membershipRepository.findByUserId(Mockito.anyLong())).thenReturn(Optional.of(membership));
        Mockito.when(membershipConverter.convert(Mockito.any(Membership.class)))
                .thenReturn(getMembershipResponse(1L));

        // when
        MembershipResponse membershipResponse = membershipService.getUserMembership(1L);

        // then
        assertThat(membershipResponse).isNotNull();
        assertEquals(membershipResponse.getId(), membership.getId());
        assertEquals(membershipResponse.getUserId(), membership.getUser().getId());
    }

    @Test
    void it_should_throw_user_not_found_exception_when_user_is_null_get_membership_by_id() {

        // given

        // when
        Throwable exception = catchThrowable(() -> membershipService.getMembershipById(1L));

        // then
        assertThat(exception).isInstanceOf(MembershipNotFoundException.class);
    }

    @Test
    void it_should_get_membership_by_id() {

        // given
        Membership membership = getMembership();
        membership.setId(1L);
        Mockito.when(membershipRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(membership));

        // when
        Membership response = membershipService.getMembershipById(1L);

        // then
        assertThat(response).isNotNull();
        assertEquals(response.getId(), membership.getId());
    }

    @Test
    void it_should_throw_user_not_found_exception_when_user_is_null_get_membership_by_user_id() {

        // given

        // when
        Throwable exception = catchThrowable(() -> membershipService.getMembershipByUserId(1L));

        // then
        assertThat(exception).isInstanceOf(MembershipNotFoundByUserIdException.class);
    }

    @Test
    void it_should_get_membership_by_user_id() {

        // given
        Membership membership = getMembership();
        membership.setId(1L);
        User user = getUser();
        user.setId(1L);
        membership.setUser(user);
        Mockito.when(membershipRepository.findByUserId(Mockito.anyLong())).thenReturn(Optional.of(membership));

        // when
        Membership response = membershipService.getMembershipByUserId(1L);

        // then
        assertThat(response).isNotNull();
        assertEquals(response.getId(), membership.getId());
        assertEquals(response.getUser().getId(), membership.getUser().getId());
    }

    @Test
    void it_should_get_total_number_of_memberships() {

        // given
        Mockito.when(membershipRepository.count()).thenReturn(Long.valueOf(getMemberships().size()));

        // when
        Long response = membershipService.getTotalNumberOfMemberships();

        // then
        assertThat(response).isNotNull();
        assertEquals(response, getMemberships().size());
    }

    private PaymentTransferGetRequest getPaymentTransferRequest() {
        return new PaymentTransferGetRequest("TR 000212564899147532", "TR 090002562311458900", "MEDIUM", 1L);
    }

    private MailRequest getMailRequest() {
        return new MailRequest("tester", "test@gmail.com", "test-user", "test message");
    }

    private PaymentResponse getPaymentCardPaymentAcceptedResponse() {
        return new PaymentResponse(1L, "Payment successful, Enjoy!!", 1L, PaymentType.CARD, PaymentStatus.ACCEPTED);
    }

    private PaymentResponse getPaymentCardPaymentRefusedResponse() {
        return new PaymentResponse(null, "Payment failed, try again!", 1L, PaymentType.CARD, PaymentStatus.REFUSED);
    }

    private PaymentCardSendRequest getPaymentCardSendRequest() {
        return new PaymentCardSendRequest("4441113332", LocalDate.now(Clock.systemDefaultZone()), "131", 1L, "tester");
    }

    private List<Membership> getMemberships() {
        return List.of(getMembership(), getMembership());
    }

    private Membership getMembership() {
        return new Membership(LocalDate.now(Clock.systemDefaultZone()), LocalDate.now(Clock.systemDefaultZone()), getUser(), MembershipStatus.ACTIVE);
    }

    private User getUser() {
        return new User("tester", "test-user", "test@gmail.com", "Test-password123", UserRole.USER);
    }

    private PaymentCardGetRequest getPaymentCardRequest() {
        return new PaymentCardGetRequest("4441113332", LocalDate.now(Clock.systemDefaultZone()), "131", 1L);
    }

    private PaymentResponse getPaymentTransferPaymentRefusedResponse() {
        return new PaymentResponse(2L, "Payment failed, try again!", 1L, PaymentType.TRANSFER, PaymentStatus.REFUSED);
    }

    private PaymentResponse getPaymentTransferAcceptedResponse() {
        return new PaymentResponse(2L, "Payment successful, Enjoy!!", 1L, PaymentType.TRANSFER, PaymentStatus.ACCEPTED);
    }

    private PaymentTransferSendRequest getPaymentTransferSendRequest() {
        return new PaymentTransferSendRequest("TR 000212564899147532", "TR 090002562311458900", "MEDIUM", 1L, "tester");
    }

    private RenewMembershipCardRequest getRenewMembershipCardRequest() {
        return new RenewMembershipCardRequest(1L, 1L);
    }

    private CardInfo getCard() {
        return new CardInfo("4441113332", LocalDate.now(Clock.systemDefaultZone()), "131", getUser());
    }

    private List<MembershipResponse> getAllMembershipResponses() {
        return List.of(getMembershipResponse(1L), getMembershipResponse(2L));
    }

    private MembershipResponse getMembershipResponse(Long id) {
        return new MembershipResponse(id, LocalDate.now(Clock.systemDefaultZone()),
                LocalDate.now(Clock.systemDefaultZone()), 1L, MembershipStatus.ACTIVE);
    }

}