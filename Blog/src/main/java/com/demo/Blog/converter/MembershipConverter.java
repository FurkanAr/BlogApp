package com.demo.Blog.converter;

import com.demo.Blog.model.Membership;
import com.demo.Blog.model.User;
import com.demo.Blog.model.enums.MembershipStatus;
import com.demo.Blog.response.MembershipResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class MembershipConverter {

    Logger logger = LoggerFactory.getLogger(getClass());

    public MembershipResponse convert(Membership membership){
        logger.info("convert to Response method started");
        MembershipResponse membershipResponse = new MembershipResponse();
        membershipResponse.setId(membership.getId());
        membershipResponse.setExpireDate(membership.getExpireDate());
        membershipResponse.setStartDate(membership.getStartDate());
        membershipResponse.setStatus(membership.getStatus());
        membershipResponse.setUserId(membership.getUser().getId());
        logger.info("convert to Response method successfully worked");
        return membershipResponse;
    }

    public Membership convert(User user){
        logger.info("convert to Membership method started");
        Membership membership = new Membership();
        membership.setUser(user);
        membership.setStatus(MembershipStatus.ACTIVE);
        membership.setStartDate(LocalDate.now());
        membership.setExpireDate(membership.getStartDate().plusMonths(1));
        logger.info("convert to Membership method successfully worked");
        return membership;
    }

    public List<MembershipResponse> convert(List<Membership> memberships){
        logger.info("convert memberships to membershipResponses method started");
        List<MembershipResponse> membershipResponses = new ArrayList<>();
        memberships.forEach(membership -> membershipResponses.add(convert(membership)));
        logger.info("convert memberships to membershipResponses method successfully worked");
        return  membershipResponses;
    }

}
