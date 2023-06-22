package com.demo.Blog.utils;

import com.demo.Blog.client.model.enums.PaymentStatus;
import com.demo.Blog.client.response.PaymentResponse;
import com.demo.Blog.exception.membership.UserHasMembershipException;
import com.demo.Blog.exception.messages.Messages;
import com.demo.Blog.exception.payment.PaymentRefusedException;
import com.demo.Blog.model.Membership;

import java.time.LocalDate;

public class MembershipUtil {

    private MembershipUtil(){}

    public static void checkMembership(Membership membership) {
        if(membership != null){
            throw new UserHasMembershipException(Messages.Membership.EXIST);
        }
    }

    public static boolean isMembershipActive(Membership membership){
        return membership.getExpireDate().isAfter(LocalDate.now());
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
