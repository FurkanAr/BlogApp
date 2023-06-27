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
        if(membership != null){
            throw new UserHasMembershipException(Messages.Membership.EXIST);
        }
    }

    public static boolean isMembershipActive(Membership membership){
        logger.debug("isMembershipActive method started");
        boolean isActive = membership.getExpireDate().isAfter(LocalDate.now());
        logger.info("User: {}, membership active: {} ", membership.getUser().getId(), isActive);
        logger.info("isMembershipActive method successfully worked");
        return isActive;
    }

    public static void checkPaymentResponse(PaymentResponse paymentResponse){
        if(PaymentStatus.REFUSED.equals(paymentResponse.getStatus()))
            throw new PaymentRefusedException(Messages.Payment.REFUSED);
    }

    public static void extractMembershipExpireDate(Membership membership) {
        LocalDate expireDate = membership.getExpireDate().plusMonths(1);
        membership.setExpireDate(expireDate);
    }
}
