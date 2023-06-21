package com.demo.Blog.Payment.repository;

import com.demo.Blog.Payment.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
}
