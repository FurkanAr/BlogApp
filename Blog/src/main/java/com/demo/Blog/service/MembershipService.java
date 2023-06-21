package com.demo.Blog.service;

import com.demo.Blog.client.PaymentServiceClient;
import com.demo.Blog.client.converter.PaymentConverter;
import com.demo.Blog.client.request.*;
import com.demo.Blog.client.response.PaymentResponse;
import com.demo.Blog.config.rabbitMQ.RabbitMQMailConfiguration;
import com.demo.Blog.converter.MailConverter;
import com.demo.Blog.converter.MembershipConverter;
import com.demo.Blog.model.CardInfo;
import com.demo.Blog.model.Membership;
import com.demo.Blog.model.User;
import com.demo.Blog.repository.MembershipRepository;
import com.demo.Blog.request.MailRequest;
import com.demo.Blog.response.MembershipResponse;
import com.demo.Blog.utils.MembershipUtil;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MembershipService {

    @Value("${membership.renewed.mail.message}")
    private String MEMBERSHIP_RENEWED;
    @Value("${membership.created.mail.message}")
    private  String MEMBERSHIP_CREATED;

    private final CardService cardService;
    private final UserService userService;

    private final MembershipRepository membershipRepository;
    private final MembershipConverter membershipConverter;
    private final PaymentConverter paymentConverter;
    private final PaymentServiceClient paymentServiceClient;
    private final RabbitMQMailConfiguration rabbitMQMailConfiguration;
    private final RabbitTemplate rabbitTemplate;
    private final MailConverter mailConverter;


    public MembershipService(CardService cardService, UserService userService, MembershipRepository membershipRepository, MembershipConverter membershipConverter, PaymentConverter paymentConverter, PaymentServiceClient paymentServiceClient, RabbitMQMailConfiguration rabbitMQMailConfiguration, RabbitTemplate rabbitTemplate, MailConverter mailConverter) {
        this.cardService = cardService;
        this.userService = userService;
        this.membershipRepository = membershipRepository;
        this.membershipConverter = membershipConverter;
        this.paymentConverter = paymentConverter;
        this.paymentServiceClient = paymentServiceClient;
        this.rabbitMQMailConfiguration = rabbitMQMailConfiguration;
        this.rabbitTemplate = rabbitTemplate;
        this.mailConverter = mailConverter;
    }

    public String createMembershipPayByCard(PaymentCardGetRequest paymentCardGetRequest) {
        User user = userService.findUserById(paymentCardGetRequest.getUserId());

        MembershipUtil.checkMembership(user.getMembership());

        PaymentCardSendRequest paymentCardSendRequest = paymentConverter.convert(user, paymentCardGetRequest);

        PaymentResponse paymentResponse = paymentServiceClient.create(paymentCardSendRequest);

        if(MembershipUtil.checkPaymentResponse(paymentResponse))
            return paymentResponse.getMessage();

        membershipRepository.save(membershipConverter.convert(user));
        MailRequest mailRequest = mailConverter.convert(user, MEMBERSHIP_CREATED);

        rabbitTemplate.convertAndSend(rabbitMQMailConfiguration.getQueueName(), mailRequest );
        cardService.saveCard(paymentCardGetRequest,user);

        return paymentResponse.getMessage();
    }

    public String renewMembershipCardRequest(RenewMembershipCardRequest renewMembershipRequest) {
        Membership membership = getMembershipByUserId(renewMembershipRequest.getUserId());
        CardInfo cardInfo = cardService.getCardByUserId(renewMembershipRequest.getUserId());

        PaymentCardSendRequest paymentCardSendRequest = paymentConverter.getCardInfoForPayment(membership, cardInfo);
        PaymentResponse paymentResponse = paymentServiceClient.create(paymentCardSendRequest);

        if(MembershipUtil.checkPaymentResponse(paymentResponse))
            return paymentResponse.getMessage();

        extractMembershipExpireDate(membership);

        membershipRepository.save(membership);

        MailRequest mailRequest = mailConverter.convert(membership.getUser(), MEMBERSHIP_RENEWED);
        rabbitTemplate.convertAndSend(rabbitMQMailConfiguration.getQueueName(), mailRequest);

        return MEMBERSHIP_RENEWED;
    }

    public String createMembershipPayByTransfer(PaymentTransferGetRequest paymentTransferGetRequest) {
        User user = userService.findUserById(paymentTransferGetRequest.getUserId());

        MembershipUtil.checkMembership(user.getMembership());

        PaymentTransferSendRequest paymentTransferSendRequest = paymentConverter.convert(user, paymentTransferGetRequest);

        PaymentResponse paymentResponse = paymentServiceClient.create(paymentTransferSendRequest);

        if(MembershipUtil.checkPaymentResponse(paymentResponse))
            return paymentResponse.getMessage();

        membershipRepository.save(membershipConverter.convert(user));

        MailRequest mailRequest = mailConverter.convert(user, MEMBERSHIP_CREATED);
        rabbitTemplate.convertAndSend(rabbitMQMailConfiguration.getQueueName(), mailRequest);

        return paymentResponse.getMessage();
    }

    public String renewMembershipTransferRequest(PaymentTransferGetRequest paymentTransferGetRequest) {
        Membership membership =getMembershipByUserId(paymentTransferGetRequest.getUserId());

        PaymentTransferSendRequest paymentTransferSendRequest = paymentConverter.convert(membership.getUser(), paymentTransferGetRequest);

        PaymentResponse paymentResponse = paymentServiceClient.create(paymentTransferSendRequest);

        if(MembershipUtil.checkPaymentResponse(paymentResponse))
            return paymentResponse.getMessage();

        extractMembershipExpireDate(membership);
        membershipRepository.save(membership);

        MailRequest mailRequest = mailConverter.convert(membership.getUser(), MEMBERSHIP_RENEWED);
        rabbitTemplate.convertAndSend(rabbitMQMailConfiguration.getQueueName(), mailRequest);

        return MEMBERSHIP_RENEWED;
    }


    protected Membership getMembershipById(Long membershipId){
        return membershipRepository.findById(membershipId).orElseThrow(() -> new RuntimeException("memebership cannot found!"));
    }

    protected Membership getMembershipByUserId(Long userId){
        return membershipRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("memebership cannot found with userId: " + userId));
    }

    private static void extractMembershipExpireDate(Membership membership){
        LocalDate expireDate = membership.getExpireDate().plusMonths(1);
        membership.setExpireDate(expireDate);
    }


    public String deleteMembershipById(Long membershipId) {
        Membership membership = getMembershipById(membershipId);
        System.out.println("membership: " + membership);
        membershipRepository.deleteById(membershipId);
        System.out.println("membership: " + membership);
        System.out.println("membershipId: " + membershipId);

        return membership.getId().toString();
    }

    public List<MembershipResponse> getAllMemberships() {
        return membershipConverter.convert(membershipRepository.findAll());
    }

    public MembershipResponse getOneMembership(Long membershipId) {
        return membershipConverter.convert(getMembershipById(membershipId));
    }

    public MembershipResponse getUserMembership(Long userId) {
        return membershipConverter.convert(getMembershipByUserId(userId));
    }
}
