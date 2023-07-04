package com.demo.Blog.utils;

import com.demo.Blog.client.model.enums.PaymentStatus;
import com.demo.Blog.client.response.PaymentResponse;
import com.demo.Blog.exception.membership.UserHasMembershipException;
import com.demo.Blog.exception.messages.Messages;
import com.demo.Blog.exception.payment.PaymentRefusedException;
import com.demo.Blog.model.Membership;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

public class MembershipUtil {
    static Logger logger = LoggerFactory.getLogger(MembershipUtil.class);

    private MembershipUtil(){}

    public static void checkMembership(Membership membership) {
        logger.info("checkMembership method started");
        if(membership != null){
            logger.warn("User: {}, has membership: {} ", membership.getUser().getId(), membership.getId());
            throw new UserHasMembershipException(Messages.Membership.EXIST);
        }
        logger.info("Membership not found");
        logger.info("checkMembership method successfully worked");
    }

    public static boolean isMembershipActive(Membership membership){
        logger.info("isMembershipActive method started");
        boolean isActive = membership.getExpireDate().isAfter(LocalDate.now());
        logger.info("User: {}, membership active: {} ", membership.getUser().getId(), isActive);
        logger.info("isMembershipActive method successfully worked");
        return !isActive;
    }

    public static void checkPaymentResponse(PaymentResponse paymentResponse){
        logger.info("checkPaymentResponse method started");
        logger.info("PaymentResponse: {}, user: {} ", paymentResponse.getStatus(), paymentResponse.getUserId());
        if(PaymentStatus.REFUSED.equals(paymentResponse.getStatus())){
            logger.warn("Payment refused");
            throw new PaymentRefusedException(Messages.Payment.REFUSED);
        }
        logger.info("checkPaymentResponse method successfully worked");
    }

    public static void extractMembershipExpireDate(Membership membership) {
        logger.info("extractMembershipExpireDate method started");
        LocalDate expireDate = membership.getExpireDate().plusMonths(1);
        membership.setExpireDate(expireDate);
        logger.info("User: {}, membership extended: {} ", membership.getUser().getId(), membership.getId());
        logger.info("extractMembershipExpireDate method successfully worked");
    }
}
