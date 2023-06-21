package com.demo.Blog.repository;

import com.demo.Blog.model.CardInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface CardInfoRepository extends JpaRepository<CardInfo, Long> {


    Optional<CardInfo> findByUserId(Long userId);
}
