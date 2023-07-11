package com.demo.Blog.service;

import com.demo.Blog.client.PaymentServiceClient;
import com.demo.Blog.client.converter.PaymentConverter;
import com.demo.Blog.client.request.*;
import com.demo.Blog.client.response.PaymentResponse;
import com.demo.Blog.config.rabbitMQ.RabbitMQMailConfiguration;
import com.demo.Blog.constants.Constant;
import com.demo.Blog.converter.MailConverter;
import com.demo.Blog.converter.MembershipConverter;
import com.demo.Blog.exception.membership.MembershipNotFoundByUserIdException;
import com.demo.Blog.exception.membership.MembershipNotFoundException;
import com.demo.Blog.exception.messages.Messages;
import com.demo.Blog.model.CardInfo;
import com.demo.Blog.model.Membership;
import com.demo.Blog.model.User;
import com.demo.Blog.repository.MembershipRepository;
import com.demo.Blog.request.MailRequest;
import com.demo.Blog.response.MembershipResponse;
import com.demo.Blog.utils.MembershipUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MembershipService {

    private final CardService cardService;
    private final UserService userService;
    private final MembershipRepository membershipRepository;
    private final MembershipConverter membershipConverter;
    private final PaymentConverter paymentConverter;
    private final PaymentServiceClient paymentServiceClient;
    private final RabbitMQMailConfiguration rabbitMQMailConfiguration;
    private final RabbitTemplate rabbitTemplate;
    private final MailConverter mailConverter;
    Logger logger = LoggerFactory.getLogger(getClass());

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

    @Transactional
    public PaymentResponse createMembershipPayByCard(PaymentCardGetRequest paymentCardGetRequest) {
        logger.info("createMembershipPayByCard method started");
        User user = userService.findUserById(paymentCardGetRequest.getUserId());

        MembershipUtil.checkMembership(user.getMembership());

        PaymentCardSendRequest paymentCardSendRequest = paymentConverter.convert(user, paymentCardGetRequest);
        logger.info("PaymentCardSendRequest by user: {} ", paymentCardGetRequest.getUserId());

        logger.info("Payment request send to Payment Service");
        PaymentResponse paymentResponse = paymentServiceClient.create(paymentCardSendRequest);
        logger.info("Payment response received from Payment Service response: {}", paymentResponse.getStatus());
        MembershipUtil.checkPaymentResponse(paymentResponse);

        Membership membership = membershipRepository.save(membershipConverter.convert(user));
        logger.info("Membership created: {} ", membership.getId());

        rabbitHelper(user, Constant.Membership.MEMBERSHIP_CREATED);

        cardService.saveCard(paymentCardGetRequest, user);
        logger.info("createMembershipPayByCard method successfully worked");
        return paymentResponse;
    }

    @Transactional
    public PaymentResponse createMembershipPayByTransfer(PaymentTransferGetRequest paymentTransferGetRequest) {
        logger.info("createMembershipPayByTransfer method started");
        User user = userService.findUserById(paymentTransferGetRequest.getUserId());

        MembershipUtil.checkMembership(user.getMembership());

        PaymentTransferSendRequest paymentTransferSendRequest = paymentConverter.convert(user, paymentTransferGetRequest);
        logger.info("PaymentTransferSendRequest by user: {} ", paymentTransferSendRequest.getUserId());

        logger.info("Payment request send to Payment Service");
        PaymentResponse paymentResponse = paymentServiceClient.create(paymentTransferSendRequest);
        logger.info("Payment response received from Payment Service");

        MembershipUtil.checkPaymentResponse(paymentResponse);

        Membership membership = membershipRepository.save(membershipConverter.convert(user));
        logger.info("Membership created: {} ", membership.getId());

        rabbitHelper(user, Constant.Membership.MEMBERSHIP_CREATED);
        logger.info("createMembershipPayByTransfer method successfully worked");
        return paymentResponse;
    }

    @Transactional
    public PaymentResponse renewMembershipCardRequest(RenewMembershipCardRequest renewMembershipRequest) {
        logger.info("renewMembershipCardRequest method started");

        Membership membership = getMembershipByUserId(renewMembershipRequest.getUserId());
        CardInfo cardInfo = cardService.getCardByUserId(renewMembershipRequest.getUserId());

        PaymentCardSendRequest paymentCardSendRequest = paymentConverter.getCardInfoForPayment(membership, cardInfo);
        logger.info("PaymentCardSendRequest by user: {} ", paymentCardSendRequest.getUserId());

        logger.info("Payment request send to Payment Service");
        PaymentResponse paymentResponse = paymentServiceClient.create(paymentCardSendRequest);
        logger.info("Payment response received from Payment Service");

        MembershipUtil.checkPaymentResponse(paymentResponse);

        MembershipUtil.extractMembershipExpireDate(membership);

        membershipRepository.save(membership);
        logger.info("Membership extended: {} ", membership.getId());

        rabbitHelper(membership.getUser(), Constant.Membership.MEMBERSHIP_RENEWED);
        logger.info("renewMembershipCardRequest method successfully worked");
        return paymentResponse;
    }

    @Transactional
    public PaymentResponse renewMembershipTransferRequest(PaymentTransferGetRequest paymentTransferGetRequest) {
        logger.info("renewMembershipTransferRequest method started");

        Membership membership = getMembershipByUserId(paymentTransferGetRequest.getUserId());

        PaymentTransferSendRequest paymentTransferSendRequest = paymentConverter.convert(membership.getUser(), paymentTransferGetRequest);
        logger.info("PaymentTransferSendRequest by user: {} ", paymentTransferSendRequest.getUserId());

        logger.info("Payment request send to Payment Service");
        PaymentResponse paymentResponse = paymentServiceClient.create(paymentTransferSendRequest);
        logger.info("Payment response received from Payment Service");

        MembershipUtil.checkPaymentResponse(paymentResponse);

        MembershipUtil.extractMembershipExpireDate(membership);

        membershipRepository.save(membership);
        logger.info("Membership extended: {} ", membership.getId());

        rabbitHelper(membership.getUser(),  Constant.Membership.MEMBERSHIP_RENEWED);
        logger.info("renewMembershipTransferRequest method successfully worked");
        return paymentResponse;
    }

    private void rabbitHelper(User user, String message) {
        logger.info("rabbitHelper method started");
        MailRequest mailRequest = mailConverter.convert(user, message);
        rabbitTemplate.convertAndSend(rabbitMQMailConfiguration.getQueueName(), mailRequest);
        logger.warn("Mail request: {}, sent to : {}",
                mailRequest, rabbitMQMailConfiguration.getQueueName());
        logger.info("rabbitHelper method successfully worked");
    }

    public String deleteMembershipById(Long membershipId) {
        logger.info("deleteMembershipById method started");
        Membership membership = getMembershipById(membershipId);
        membershipRepository.delete(membership);
        logger.info("Membership deleted: {} ", membershipId);
        logger.info("deleteMembershipById method successfully worked");
        return membership.getId().toString();
    }

    public List<MembershipResponse> getAllMemberships() {
        logger.info("getAllMemberships method started");
        List<Membership> memberships = membershipRepository.findAll();
        logger.info("getAllMemberships method successfully worked");
        return membershipConverter.convert(memberships);
    }

    public MembershipResponse getOneMembership(Long membershipId) {
        logger.info("getOneMembership method started");
        logger.info("getOneMembership method successfully worked");
        return membershipConverter.convert(getMembershipById(membershipId));
    }

    public MembershipResponse getUserMembership(Long userId) {
        logger.info("getUserMembership method started");
        logger.info("getUserMembership method successfully worked");
        return membershipConverter.convert(getMembershipByUserId(userId));
    }

    protected Membership getMembershipById(Long membershipId) {
        logger.info("getMembershipById method started");

        Membership membership = membershipRepository.findById(membershipId).orElseThrow(() ->
                new MembershipNotFoundException(Messages.Membership.NOT_EXISTS_BY_ID + membershipId));
        logger.info("Found membership by membershipId: {} ", membershipId);

        logger.info("getMembershipById method successfully worked");
        return membership;
    }

    protected Membership getMembershipByUserId(Long userId) {
        logger.info("getMembershipByUserId method started");
        userService.findUserById(userId);

        Membership membership = membershipRepository.findByUserId(userId).orElseThrow(() ->
                new MembershipNotFoundByUserIdException(Messages.Membership.NOT_EXIST_BY_USER_ID + userId));
        logger.info("Found membership by userId: {} ", userId);

        logger.info("getMembershipByUserId method successfully worked");
        return membership;
    }

    protected Long getTotalNumberOfMemberships() {
        logger.info("getTotalNumberOfMemberships method started");

        Long total = membershipRepository.count();
        logger.info("Number of memberships: {} ", total);

        logger.info("getTotalNumberOfMemberships method successfully worked");
        return total;
    }
}
