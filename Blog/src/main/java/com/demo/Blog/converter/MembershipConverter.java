package com.demo.Blog.converter;

import com.demo.Blog.model.Membership;
import com.demo.Blog.model.User;
import com.demo.Blog.model.enums.MembershipStatus;
import com.demo.Blog.response.MembershipResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class MembershipConverter {

    public Membership convert(User user){
        Membership membership = new Membership();
        membership.setUser(user);
        membership.setStatus(MembershipStatus.ACTIVE);
        membership.setStartDate(LocalDate.now());
        membership.setExpireDate(membership.getStartDate().plusMonths(1));
        return membership;
    }

    public MembershipResponse convert(Membership membership){
        MembershipResponse membershipResponse = new MembershipResponse();
        membershipResponse.setId(membership.getId());
        membershipResponse.setExpireDate(membership.getExpireDate());
        membershipResponse.setStartDate(membership.getStartDate());
        membershipResponse.setStatus(membership.getStatus());
        membershipResponse.setUserId(membership.getUser().getId());
        return membershipResponse;
    }

    public List<MembershipResponse> convert(List<Membership> memberships){
        List<MembershipResponse> membershipResponses = new ArrayList<>();
        memberships.stream().forEach(membership -> membershipResponses.add(convert(membership)));
        return  membershipResponses;
    }

}
