package com.demo.Blog.Payment.repository;

import com.demo.Blog.Payment.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @Query(value = "SELECT sum(price) From Payment")
    public BigDecimal sumTotal();

    @Query(value = "SELECT sum(price) From Payment WHERE user_id = :userId")
    BigDecimal sumByUserId(@Param("userId") Long userId);
}
