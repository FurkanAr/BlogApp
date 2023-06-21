package com.demo.Blog.utils;

import com.demo.Blog.client.model.enums.PaymentStatus;
import com.demo.Blog.client.response.PaymentResponse;
import com.demo.Blog.model.Membership;

import java.time.LocalDate;

public class MembershipUtil {

    private MembershipUtil(){}

    public static void checkMembership(Membership membership) {
        if(membership != null){
            throw new RuntimeException("user has membership");
        }
    }

    public static boolean isMembershipActive(Membership membership){
        return membership.getExpireDate().isAfter(LocalDate.now());
    }

    public static boolean checkPaymentResponse(PaymentResponse paymentResponse){
        return PaymentStatus.REFUSED.equals(paymentResponse.getStatus());
    }
}
