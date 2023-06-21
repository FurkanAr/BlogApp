package com.demo.Blog.repository;

import com.demo.Blog.model.Membership;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MembershipRepository extends JpaRepository<Membership, Long> {

    Optional<Membership> findByUserId(Long userId);


    List<Membership> findAllById(Long id);
}
